/*
 * Copyright (c) 2007 by Eyal Golan
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ui.combobox;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.swing.JComboBox;

/**
 * @author egolan
 *
 */
public abstract class MidiComboBox extends JComboBox {
    public MidiComboBox() {
        addItem("None");
        init();
    }
    
    public final void setNoneSelected() {
        setSelectedIndex(0);
    }
    
    private void init() {
        MidiDevice.Info midiInfos[] = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info mdi : midiInfos) {
            try {
                MidiDevice md = MidiSystem.getMidiDevice(mdi);
                if (shouldAddItem(md)) {
                    addItem(mdi);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSelectedItem(String itemByString) {
        int selected = 0;
        final int numOfItems = this.getItemCount();
        for (int i=0;i<numOfItems;i++) {
            if (getItemAt(i).toString().equals(itemByString)) {
                selected = i;
                break;
            }
        }
        super.setSelectedIndex(selected);
    }

    protected abstract boolean shouldAddItem(MidiDevice md);
}
