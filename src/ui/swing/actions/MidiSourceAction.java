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

import midi.MidiHandler;
import ui.MainFrame;
import ui.event.ConnectionEvent;
import ui.event.ConnectionListener;
import ui.panels.MidiSourcePanel;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class MidiSourceAction extends BaseAction implements ConnectionListener {
    private final MainFrame mainFrame;
    private final MidiHandler midiHandler;

    public MidiSourceAction(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.midiHandler = this.mainFrame.getMidiHandler();
        this.midiHandler.addConnectionListener(this);
        config.get("midisource").read(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MidiSourcePanel.showDialog(mainFrame, midiHandler.getMidiDeviceInfo());
    }

    @Override
    public void connected(ConnectionEvent connectionEvent) {
        setEnabled(false);
    }

    @Override
    public void disconnected() {
        setEnabled(true);
    }
}
