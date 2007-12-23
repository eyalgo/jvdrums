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
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kits.TdKit;
import managers.TDManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import ui.MainFrame;
import ui.lists.OutputKitsList;
import ui.lists.OutputKitsList.Direction;
import utils.VDrumsUtils;

/**
 * @author egolan
 */
public final class KitPanelOutput extends KitsPanel {
    private static final long serialVersionUID = -5217989811338648085L;
    private JButton saveButton;
    private JButton sendToModuleButton;
    private JButton removeKitButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private JButton clearButton;

    public KitPanelOutput(MainFrame parentFrame) {
        super(parentFrame, new OutputKitsList());
        saveButton = new JButton(saveToFile());
        saveButton.setToolTipText("Save to file");
        
        sendToModuleButton = new JButton(sendToModeule());
        sendToModuleButton.setToolTipText("Send kits to module");
        sendToModuleButton.setEnabled(false);
        
        removeKitButton = new JButton(removeFromList());
        removeKitButton.setToolTipText("Remove from list");
        
        moveDownButton = new OutputButton("Move down", "down-32x32.png",
                Direction.DECREASE_INDEX, VDrumsUtils.MAX_NUMBER_OF_KITS - 1);
        moveUpButton = new OutputButton("Move up", "up-32x32.png", Direction.INCREASE_INDEX, 0);
        
        clearButton = new JButton(clearList());
        clearButton.setToolTipText("Clear list");
        
        addToButtonBar(saveButton);
        addToButtonBar(sendToModuleButton);
        addToButtonBar(removeKitButton);
        addToButtonBar(clearButton);
        addToButtonBar(moveUpButton);
        addToButtonBar(moveDownButton);
        moveDownButton.setEnabled(false);
        moveUpButton.setEnabled(false);
    }

    @SuppressWarnings("serial")
    private Action sendToModeule() {
        Icon icon = createIcon("upload-to-device.png");
        Action action = new AbstractAction("", icon) {
            public void actionPerformed(ActionEvent e) {
            }
        };
        return action;
    }

    @SuppressWarnings("serial")
    private Action removeFromList() {
        Icon icon = createIcon("delete-32x32.png");
        Action action = new AbstractAction("", icon) {
            public void actionPerformed(ActionEvent e) {
                ((OutputKitsList) getKitList()).deleteSelectedKit();
            }
        };
        return action;
    }

    @SuppressWarnings("serial")
    private Action saveToFile() {
        Icon icon = createIcon("save-as-32x32.png");
        Action action = new AbstractAction("", icon) {
            public void actionPerformed(ActionEvent e) {
                final TdKit[] kitsInList = getKitList().getKits();
                if (((OutputKitsList) getKitList()).numberOfKits() < 1) {
                    getParentFrame().showErrorDialog("There aren't any kits in the list.",
                            "No kits problem");
                    return;
                }
                JFileChooser fc = createFileChooser("saveFileChooser");
                int option = fc.showSaveDialog(getParentFrame());

                if (JFileChooser.APPROVE_OPTION == option) {
                    final File file = fc.getSelectedFile();
                    try {
                        saveKits(file, kitsInList);
                    }
                    catch (InvalidMidiDataException e1) {
                        getParentFrame().showErrorDialog(e1.getMessage(), e1.getMessage());
                        e1.printStackTrace();
                    }
                    catch (IOException e1) {
                        getParentFrame().showErrorDialog(e1.getMessage(), e1.getMessage());
                        e1.printStackTrace();
                    }
                    catch (Exception e9) {
                        getParentFrame().showErrorDialog(e9.getMessage(), e9.getMessage());
                    }
                    catch (Error er) {
                        getParentFrame().showErrorDialog("Fatal Error", er.getMessage());
                    }
                }
            }

            private void saveKits(File file, TdKit[] kitsInList)
                    throws InvalidMidiDataException, IOException {
                if (!FilenameUtils.isExtension(file.getName(), "syx")) {
                    String name = file.getAbsolutePath() + ".syx";
                    file = new File(name);
                }
                final SysexMessage messageFromManager = TDManager
                        .kitsToSysexMessage(kitsInList);
                FileUtils.writeByteArrayToFile(file, messageFromManager.getMessage());
            }
        };
        return action;
    }

    @SuppressWarnings("serial")
    private class OutputButton extends JButton implements ListSelectionListener {
        private final int disabledIndex;

        private OutputButton(final String tooltip, final String iconFileName,
                final Direction direction, final int disabledIndex) {
            super();
            setAction(moveOperation(direction, "", iconFileName));
            setToolTipText(tooltip);
            getKitList().addListSelectionListener(this);
            this.disabledIndex = disabledIndex;
        }

        @SuppressWarnings("serial")
        private Action moveOperation(final Direction direction, final String label,
                final String iconFileName) {
            Icon icon = createIcon(iconFileName);
            Action action = new AbstractAction(label, icon) {
                public void actionPerformed(ActionEvent e) {
                    ((OutputKitsList) getKitList()).moveSelection(direction);
                }
            };
            return action;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedIndex = ((OutputKitsList) e.getSource()).getSelectedIndex();
            TdKit selectedKit = (TdKit) ((OutputKitsList) e.getSource()).getSelectedValue();
            setEnabled(selectedKit != null && selectedIndex != this.disabledIndex);
        }

    }
}
