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

import kits.info.Td12Info;
import kits.info.Td6Info;
import kits.info.TdInfo;
import ui.event.ConnectionEvent;
import ui.event.ConnectionListener;
import exceptions.NotRolandException;
import exceptions.UnsupportedModuleException;
import exceptions.VdrumException;

/**
 * @author Limor Eyal
 *
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
    public void close() {
    }

    @Override
    public void send(MidiMessage midiMessage, long timeStamp) {
        if (midiMessage instanceof SysexMessage) {
            try {
                TdInfo tdInfo = getTdInfoByMessage(midiMessage);
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

    private TdInfo getTdInfoByMessage(MidiMessage midiMessage) throws VdrumException {
        byte[] message = midiMessage.getMessage();
        if ((message[5]& 0xFF) != 65) {
            throw new NotRolandException(message[5]);
        }
        if ((message[6]& 0xFF) == 9) {
            return new Td12Info();
        } else if ((message[6]& 0xFF) == 63) {
            return new Td6Info();
        } else {
            throw new UnsupportedModuleException();
        }
    }

}
