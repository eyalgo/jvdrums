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
import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;

import managers.TDManager;
import midi.BulkSender;
import exceptions.VdrumException;

import kits.TdKit;
import ui.MainFrame;
import ui.lists.OutputKitsList;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class SendToModuleAction extends BaseAction {
    private final MainFrame mainFrame;
    private final OutputKitsList outputKitsList;
    private final BulkSender bulkSender;

    public SendToModuleAction(MainFrame mainFrame, OutputKitsList outputKitsList) {
        this.mainFrame = mainFrame;
        this.bulkSender = this.mainFrame.getBulkSender();
        this.outputKitsList = outputKitsList;
        config.get("sendToModule").read(this);
    }

    public void actionPerformed(ActionEvent e) {
        final TdKit[] kitsInList = outputKitsList.getKits();
        if (outputKitsList.numberOfKits() < 1) {
            mainFrame.showErrorDialog("There aren't any kits in the list.", "No kits problem");
            return;
        }
        sendToModule(kitsInList);
    }

    private void sendToModule(TdKit[] kits) {
        if (bulkSender.getMidiDeviceInfo() == null) {
            mainFrame.showErrorDialog("Module Output MIDI is not set",
                    "MIDI Detination is not set");
            return;
        }
        try {
            Vector<TdKit> actualKits = TDManager.kitsToKits(kits);
            for (TdKit kit : actualKits) {
                System.out.println("Sending " + kit);
                bulkSender.sendKits(kit);
            }
        }
        catch (InvalidMidiDataException e) {
            mainFrame.showErrorDialog(e.getMessage(), e.getMessage());
            e.printStackTrace();
        }
        catch (VdrumException e) {
            mainFrame.showErrorDialog(e);
        }
    }
}