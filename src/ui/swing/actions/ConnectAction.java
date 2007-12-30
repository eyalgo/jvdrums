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

package ui.swing.actions;

import java.awt.event.ActionEvent;

import javax.sound.midi.InvalidMidiDataException;

import midi.MidiHandler;
import ui.MainFrame;
import ui.event.ConnectionEvent;
import ui.event.ConnectionListener;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class ConnectAction extends BaseAction implements ConnectionListener {
    private final MidiHandler midiHandler;
    private final MainFrame mainFrame;

    public ConnectAction(MainFrame mainFrame, MidiHandler midiHandler) {
        this.midiHandler = midiHandler;
        midiHandler.addConnectionListener(this);
        this.mainFrame = mainFrame;
        config.get("connect").read(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            midiHandler.sendRequestId();
        }
        catch (InvalidMidiDataException e1) {
            mainFrame.showErrorDialog(e1.getMessage(), e1.getMessage());
        }
    }

    @Override
    public void connected(ConnectionEvent connectionEvent) {
        config.get("disconnect").read(this);
    }

    @Override
    public void disconnected() {
        config.get("connect").read(this);
    }

}
