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

package ui.panels;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import kits.TdKit;
import managers.TDManager;

import org.apache.commons.io.FileUtils;

import ui.MainFrame;
import exceptions.VdrumException;

/**
 * @author egolan
 */
public final class KitPanelInput extends KitsPanel {
    private static final long serialVersionUID = 3267475173137048327L;
    private JButton inputDownlaodButton;
    private JButton inputLoadButton;

    public KitPanelInput(MainFrame parentFrame) {
        super(parentFrame);
        inputLoadButton = new JButton("Load");
        inputDownlaodButton = new JButton("Download");
        addToButtonBar(inputLoadButton);
        addToButtonBar(inputDownlaodButton);
        inputLoadButton.addActionListener(openFile());
    }

    @SuppressWarnings("serial")
    public Action openFile() {
        Action action = new AbstractAction() {
            JFileChooser fc = createFileChooser("openFileChooser");

            public void actionPerformed(ActionEvent e) {
                int option = fc.showOpenDialog(getParentFrame());

                if (JFileChooser.APPROVE_OPTION == option) {
                    final File file = fc.getSelectedFile();
                    try {
                        fileToList(file);
                    }
                    catch (IOException e1) {
                        getParentFrame().problem(e1.getMessage());
                        e1.printStackTrace();
                    }
                    catch (InvalidMidiDataException e1) {
                        getParentFrame().problem(e1.getMessage());
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                        System.exit(-3);
                    }
                    catch (VdrumException e2) {
                        getParentFrame().problem(e2.getMessage());
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                        System.exit(-4);
                    }
                }
            }
        };
        return action;
    }

    protected void fileToList(final File file) throws IOException, InvalidMidiDataException,
            VdrumException {
        byte[] bytes = FileUtils.readFileToByteArray(file);
        TdKit[] kits = TDManager.bytesToKits(bytes);
        for (TdKit kit : kits) {
            addKit(kit);
        }
    }

    @Override
    void kitPressed(int index) {
        getParentFrame().leftKitPressed((TdKit)getKitListModel().getElementAt(index));
    }
}
