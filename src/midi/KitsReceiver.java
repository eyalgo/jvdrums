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

package midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;
import javax.swing.SwingUtilities;

import kits.TdKit;
import kits.info.TdInfo;
import managers.TDManager;

import org.apache.commons.lang.ArrayUtils;

import ui.panels.KitPanelInput;
import exceptions.VdrumException;

/**
 * @author Limor Eyal
 */
final class KitsReceiver implements Receiver {
    private TdInfo tdInfo;
    private byte[] receivedBytes = null;
    private KitPanelInput inputPanel;

    public KitsReceiver() {
        receivedBytes = null;
    }

    @Override
    public void close() {
    }

    @Override
    public void send(MidiMessage midiMessage, long timeStamp) {
        if (midiMessage instanceof SysexMessage) {
            byte[] message = ((SysexMessage) midiMessage).getMessage();
            receivedBytes = ArrayUtils.addAll(receivedBytes, message);
//            System.out.println(receivedBytes.length);
            if (receivedBytes.length == tdInfo.getKitSize()) {
                try {
                    final TdKit kit = TDManager.bytesToOneKit(receivedBytes);
                    addToPanel(kit);
                    receivedBytes = null;
                }
                catch (InvalidMidiDataException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (VdrumException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
        }
    }

    private void addToPanel(final TdKit kit) {
        if (SwingUtilities.isEventDispatchThread()) {
            inputPanel.addKit(kit);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    inputPanel.addKit(kit);
                }
            });
        }
    }

    void setTdIdInfo(TdInfo tdInfo) {
        this.tdInfo = tdInfo;
        System.out.println("Setting TdInfo " + tdInfo + " kit size= " + tdInfo.getKitSize());
    }

    void setKitPanelInput(KitPanelInput inputPanel) {
        this.inputPanel = inputPanel;
    }
}
