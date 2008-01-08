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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JComboBox;

import kits.info.InfoManager;
import kits.info.TdInfo;
import ui.MainFrame;
import ui.event.ConnectionEvent;
import ui.event.ConnectionListener;
import ui.lists.OutputKitsList;
import ui.lists.OutputKitsList.Direction;
import ui.swing.actions.ClearListAction;
import ui.swing.actions.MoveKitAction;
import ui.swing.actions.RemoveKitsAction;
import ui.swing.actions.SaveAction;
import ui.swing.actions.SendToModuleAction;

/**
 * @author egolan
 */
public final class KitPanelOutput extends KitsPanel {
    private static final long serialVersionUID = -5217989811338648085L;
    private final OutputKitsList outputKitsList;
    private final MoveKitAction increaseIndexMoveAction;
    private final ModulesChooserCombo modulesChooserCombo;

    public KitPanelOutput(MainFrame parentFrame, TdInfo tdInfo) {
        super(parentFrame);
        outputKitsList = new OutputKitsList(this, tdInfo);
        parentFrame.addConnectionListener(outputKitsList);
        setListInPanel(outputKitsList);
        addToButtonBar(new SaveAction(getParentFrame(), this));
        addToButtonBar(new SendToModuleAction(getParentFrame(), outputKitsList));
        addToButtonBar(new RemoveKitsAction(outputKitsList));
        addToButtonBar(new ClearListAction(parentFrame, outputKitsList));
        increaseIndexMoveAction = new MoveKitAction(Direction.INCREASE_INDEX, outputKitsList,
                tdInfo.getMaxNumberOfKits() - 1);
        addToButtonBar(increaseIndexMoveAction);
        addToButtonBar(new MoveKitAction(Direction.DECREASE_INDEX, outputKitsList, 0));
        modulesChooserCombo = new ModulesChooserCombo(tdInfo);
        parentFrame.addConnectionListener(modulesChooserCombo);
        addToButtonBar(modulesChooserCombo);
    }

    public void setMaxNumberOfKits(int maxNumberOfKits) {
        increaseIndexMoveAction.setMaxNumberOfKits(maxNumberOfKits);
    }

    @SuppressWarnings("serial")
    private class ModulesChooserCombo extends JComboBox implements ConnectionListener {
        private KitPanelInput inputPanel;

        private ModulesChooserCombo(TdInfo defaultTdInfo) {
            Vector<TdInfo> availableModulesInfo = InfoManager.availableModulesInfo();
            for (TdInfo tdInfo : availableModulesInfo) {
                addItem(tdInfo);
            }
            setSelectedItem(defaultTdInfo);
            setFocusable(false);
            addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent evt) {
                    moduleTypeChanged(evt);
                }

                private void moduleTypeChanged(ItemEvent evt) {
                    switch (evt.getStateChange()) {
                        case ItemEvent.SELECTED:
                            TdInfo tdInfo = ((TdInfo) getSelectedItem());
                            increaseIndexMoveAction.setMaxNumberOfKits(tdInfo
                                    .getMaxNumberOfKits() - 1);
                            outputKitsList.setTdInfo(tdInfo);
                            inputPanel.setTdInfo(tdInfo);
                            break;
                    }
                }
            });
        }

        @Override
        public void connected(ConnectionEvent connectionEvent) {
            setSelectedItem(connectionEvent.getTdInfo());
            setEnabled(false);
        }

        @Override
        public void disconnected() {
            setEnabled(true);
        }

        private void setInputKitsPanel(KitPanelInput inputPanel) {
            this.inputPanel = inputPanel;
        }

    }

    public void initInputPanelInOutputPanel(KitPanelInput inputPanel) {
        modulesChooserCombo.setInputKitsPanel(inputPanel);
        inputPanel.setTdInfo((TdInfo) modulesChooserCombo.getSelectedItem());
    }
}
