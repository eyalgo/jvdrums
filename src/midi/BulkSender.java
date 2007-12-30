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
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

import kits.TdKit;
import kits.VdrumsSysexMessage;

/**
 * @author egolan
 */
final class BulkSender {
//  F0H 7EH, dev, 06H, 01H F7H
    public final static byte[] ID_REQUEST_BYTES;
    static {
        ID_REQUEST_BYTES = new byte[6];
        ID_REQUEST_BYTES[0] = (byte)240;
        ID_REQUEST_BYTES[1] = 126;
        ID_REQUEST_BYTES[2] = 16;
        ID_REQUEST_BYTES[3] = 6;
        ID_REQUEST_BYTES[4] = 1;
        ID_REQUEST_BYTES[5] = (byte)247;
    }
    MidiDevice.Info midiDeviceInfo = null;
    MidiDevice midiDevice = null;
    Receiver deviceReceiver = null;
    int deviceId = 16;

    BulkSender() {}
    
    MidiDevice.Info getMidiDeviceInfo() {
        return midiDeviceInfo;
    }

    void setDestinationDeviceInformation(MidiDevice.Info newMidiInfo, int deviceId)
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
                deviceReceiver = midiDevice.getReceiver();
            }
        }
        this.midiDeviceInfo = newMidiInfo;
        this.deviceId = deviceId;
    }

    void sendKits(final TdKit kit) {
        VdrumsSysexMessage[] parts = kit.getKitSubParts();
        for (VdrumsSysexMessage part : parts) {
            long timestamp = midiDevice.getMicrosecondPosition();
            deviceReceiver.send(part, timestamp);
        }
    }
    
    void sendRequestId() throws InvalidMidiDataException {
        SysexMessage sm = new SysexMessage();
        byte[] bytes = BulkSender.ID_REQUEST_BYTES;
        bytes[2] = (byte)deviceId;
        sm.setMessage(BulkSender.ID_REQUEST_BYTES, BulkSender.ID_REQUEST_BYTES.length);
        long timestamp = this.midiDevice.getMicrosecondPosition();
        deviceReceiver.send(sm, timestamp);
    }
}
