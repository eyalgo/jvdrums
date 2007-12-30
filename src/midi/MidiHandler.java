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
import javax.sound.midi.MidiUnavailableException;

import kits.TdKit;

import ui.event.ConnectionListener;
import ui.panels.KitsPanel;

/**
 * @author egolan
 */
public final class MidiHandler {
    private final BulkReciever bulkReciever;
    private final BulkSender bulkSender;

    public MidiHandler(KitsPanel inputPanel) {
        bulkReciever = new BulkReciever(inputPanel);
        bulkSender = new BulkSender();
    }

    public void addConnectionListener(ConnectionListener connectionListener) {
        bulkReciever.addConnectionListener(connectionListener);
    }

    public void setSourceAndDestination(MidiDevice.Info sourceDevice,
            MidiDevice.Info destinationDevice, int deviceId) throws MidiUnavailableException {
        bulkReciever.setSoureceDevice(sourceDevice);
        bulkSender.setDestinationDeviceInformation(destinationDevice, deviceId);
    }

    public void sendRequestId() throws InvalidMidiDataException {
        bulkSender.sendRequestId();
    }

    public MidiDevice.Info getMidiDeviceInfo() {
        return bulkSender.getMidiDeviceInfo();
    }

    public void sendKits(TdKit kit) {
        bulkSender.sendKits(kit);
    }
}
