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

import java.util.Vector;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

import kits.info.TdInfo;
import managers.FactoryKits;
import ui.event.ConnectionEvent;
import ui.event.ConnectionListener;
import exceptions.VdrumException;

/**
 * @author Limor Eyal
 */
final class DeviceIdentityReceiver implements Receiver {
    private final Vector<ConnectionListener> connectionListeners;

    DeviceIdentityReceiver() {
        connectionListeners = new Vector<ConnectionListener>();
    }

    void addConnectionListener(ConnectionListener connectionListener) {
        connectionListeners.add(connectionListener);
    }

    @Override
    public void close() {}

    @Override
    public void send(MidiMessage midiMessage, long timeStamp) {
        if (midiMessage instanceof SysexMessage) {
            try {
                TdInfo tdInfo = FactoryKits.getTdInfoByIdentityMessage(midiMessage);
                fireConnectionEvent(tdInfo);
            }
            catch (VdrumException e) {
                e.printStackTrace();
            }
        }

    }

    private void fireConnectionEvent(TdInfo tdInfo) {
        ConnectionEvent connEvent = new ConnectionEvent(tdInfo);
        for (ConnectionListener connectionListener : connectionListeners) {
            connectionListener.connected(connEvent);
        }
    }

    void disconnect() {
        fireDisconnectEvent();
    }

    private void fireDisconnectEvent() {
        for (ConnectionListener connectionListener : connectionListeners) {
            connectionListener.disconnected();
        }
    }

}
