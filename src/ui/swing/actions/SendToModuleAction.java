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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import kits.TdKit;
import managers.TDManager;
import midi.BulkSender;
import ui.MainFrame;
import ui.lists.OutputKitsList;
import exceptions.VdrumException;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class SendToModuleAction extends BaseAction implements ListDataListener {
    private final MainFrame mainFrame;
    private final OutputKitsList outputKitsList;
    private final BulkSender bulkSender;
    private String sendMessage;

    //TODO Move all string to the properties file
    public SendToModuleAction(MainFrame mainFrame, OutputKitsList outputKitsList) {
        this.mainFrame = mainFrame;
        this.bulkSender = this.mainFrame.getBulkSender();
        this.outputKitsList = outputKitsList;
        config.get("sendToModule").read(this);
        this.outputKitsList.getModel().addListDataListener(this);
        setEnabledByKits();
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
            send(kits);
        }
        catch (InvalidMidiDataException e) {
            mainFrame.showErrorDialog(e.getMessage(), e.getMessage());
            e.printStackTrace();
        }
        catch (VdrumException e) {
            mainFrame.showErrorDialog(e);
        }
    }

    private void send(final TdKit[] kits) throws InvalidMidiDataException, VdrumException {
        ensureEventThread();
        final Vector<TdKit> actualKits = TDManager.kitsToKits(kits);
        Thread worker = new Thread() {
            @Override
            public void run() {
                mainFrame.operationStart(sendMessage, Color.RED);
                for (final TdKit kit : actualKits) {
                    mainFrame.addInfo("Sending: " + kit.getName() + " to slot number "
                            + kit.getId());
                    bulkSender.sendKits(kit);
                    // TODO Remove this !!!
                    try {
                        Thread.sleep(4000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mainFrame.operationFinish();
            }
        };
        worker.start();
    }

    private void ensureEventThread() {
        if (SwingUtilities.isEventDispatchThread()) {
            return;
        }

        throw new RuntimeException("only the event " + "thread should invoke this method");
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        setEnabledByKits();
    }

    private void setEnabledByKits() {
        setEnabled(outputKitsList.numberOfKits() > 0);
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
    // Unimplemented
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
    // Unimplemented
    }

}
