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
import javax.sound.midi.Transmitter;

import ui.MainFrame;
import ui.event.ConnectionEvent;
import ui.event.ConnectionListener;
import ui.panels.KitPanelInput;

/**
 * @author Limor Eyal
 *
 */
final class BulkReciever implements ConnectionListener {
    private MidiDevice midiDevice = null;
    private MidiDevice.Info midiDeviceInfo = null;
    private Transmitter deviceTransmitter = null;
    private DeviceIdentityReceiver idRequestReceivwer;
    private KitsReceiver kitsReceiver;
    BulkReciever(MainFrame mainFrame) {
        kitsReceiver = new KitsReceiver(mainFrame);
        idRequestReceivwer = new DeviceIdentityReceiver();
        idRequestReceivwer.addConnectionListener(this);
    }
    
    void setSoureceDevice(MidiDevice.Info newMidiInfo) throws MidiUnavailableException {
        if (this.midiDevice != null) {
            this.midiDevice.close();
        }
        if (newMidiInfo != null) {
            this.midiDeviceInfo = newMidiInfo;
            System.out.println(midiDeviceInfo);
            MidiDevice newSourceDevice = MidiSystem.getMidiDevice(midiDeviceInfo);
            if (newSourceDevice != null) {
                this.midiDevice = newSourceDevice;
                this.midiDevice.open();
                deviceTransmitter = midiDevice.getTransmitter();
            }
        }
    }
    
    @Override
    public void connected(ConnectionEvent connectionEvent) {
        if (deviceTransmitter != null) {
            deviceTransmitter.setReceiver(kitsReceiver);
            kitsReceiver.setTdIdInfo(connectionEvent.getTdInfo());
        }
    }
    
    @Override
    public void disconnected() {
        if (deviceTransmitter != null) {
            deviceTransmitter.setReceiver(null);            
        }
    }

    void addConnectionListener(ConnectionListener connectionListener) {
        idRequestReceivwer.addConnectionListener(connectionListener);
    }
    
    void setKitPanelInput(KitPanelInput inputPanel) {
        kitsReceiver.setKitPanelInput(inputPanel);
    }
    
    void sendRequestId() throws MidiUnavailableException {
        deviceTransmitter.setReceiver(idRequestReceivwer);
    }

    void disconnect() {
        idRequestReceivwer.disconnect();
    }

}
