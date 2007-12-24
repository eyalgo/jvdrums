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

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import kits.TdKit;

/**
 * @author egolan
 */
public final class BulkSender {
    MidiDevice.Info midiDeviceInfo = null;
    MidiDevice midiDevice = null;
    Receiver actReceiver = null;

    public BulkSender() {}
    
    public MidiDevice.Info getMidiDeviceInfo() {
        return midiDeviceInfo;
    }

    public void setDestinationDevice(MidiDevice.Info newMidiInfo)
            throws MidiUnavailableException {
        if (this.midiDevice != null) {
            this.midiDevice.close();
        }
        if (newMidiInfo != null) {
            System.out.println(newMidiInfo);
            MidiDevice newDestinationDevice = MidiSystem.getMidiDevice(newMidiInfo);
            if (newDestinationDevice != null) {
                this.midiDevice = newDestinationDevice;
                this.midiDevice.open();
                actReceiver = midiDevice.getReceiver();
            }
        }
        this.midiDeviceInfo = newMidiInfo;
    }

    public void sendKits(final TdKit kit) {
        VdrumsSysexMessage[] parts = kit.getKitSubParts();
        for (VdrumsSysexMessage part : parts) {
            long timestamp = midiDevice.getMicrosecondPosition();
            actReceiver.send(part, timestamp);
        }
    }
}