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
import javax.sound.midi.SysexMessage;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import kits.TdKit;
import managers.TDManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import ui.MainFrame;

/**
 * @author egolan
 */
public final class KitPanelOutput extends KitsPanel {
    private static final long serialVersionUID = -5217989811338648085L;
    private JButton outputSaveButton;
    private JButton outputButtonUpload;
    private JButton outputDeleteButton;
    private JButton outputUpButton;
    private JButton outputDownButton;

    public KitPanelOutput(MainFrame parentFrame) {
        super(parentFrame);
        outputSaveButton = new JButton("Save");
        outputButtonUpload = new JButton("Upload");
        outputDeleteButton = new JButton("Delete");
        outputUpButton = new JButton("Up");
        outputDownButton = new JButton("Down");
        addToButtonBar(outputSaveButton);
        addToButtonBar(outputButtonUpload);
        addToButtonBar(outputDeleteButton);
        addToButtonBar(outputUpButton);
        addToButtonBar(outputDownButton);
        outputSaveButton.addActionListener(saveToFile());
    }

    @SuppressWarnings("serial")
    public Action saveToFile() {
        Action action = new AbstractAction() {
            JFileChooser fc = createFileChooser("saveFileChooser");

            public void actionPerformed(ActionEvent e) {
                int option = fc.showSaveDialog(getParentFrame());

                if (JFileChooser.APPROVE_OPTION == option) {
                    final File file = fc.getSelectedFile();
                    try {
                        saveKits(file);
                    }
                    catch (InvalidMidiDataException e1) {
                        getParentFrame().problem(e1.getMessage());
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    catch (IOException e1) {
                        getParentFrame().problem(e1.getMessage());
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        };
        return action;
    }

    private void saveKits(File file) throws InvalidMidiDataException, IOException {
        if (getKitListModel().getSize() < 1) {
            getParentFrame().problem("no kits");
            return;
        }
        Object[] objectKitsInList = getKitListModel().toArray();
        TdKit[] kitsInList = new TdKit[objectKitsInList.length];
        for (int i=0;i<objectKitsInList.length;i++) {
            kitsInList[i] = (TdKit)objectKitsInList[i];
        }
        if (!FilenameUtils.isExtension(file.getName(), "syx")) {
            String name = file.getAbsolutePath() + ".syx";
            file = new File(name);
        }
        SysexMessage messageFromManager = TDManager.kitsToSysexMessage(kitsInList);
        FileUtils.writeByteArrayToFile(file, messageFromManager.getMessage());
    }

    @Override
    void kitPressed(int index) {
    // TODO Auto-generated method stub

    }
}
