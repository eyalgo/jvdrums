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
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComboBox;

import kits.info.InfoManager;
import kits.info.TdInfo;
import ui.MainFrame;
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
public final class KitPanelOutput extends KitsPanel implements ActionListener {
    private static final long serialVersionUID = -5217989811338648085L;
    private final OutputKitsList outputKitsList;
    private final MoveKitAction moveKitAction;

    public KitPanelOutput(MainFrame parentFrame, TdInfo tdInfo) {
        super(parentFrame);
        outputKitsList = new OutputKitsList(this, tdInfo);
        setListInPanel(outputKitsList);
        addToButtonBar(new SaveAction(getParentFrame(), this));
        addToButtonBar(new SendToModuleAction(getParentFrame(), outputKitsList));
        addToButtonBar(new RemoveKitsAction(outputKitsList));
        addToButtonBar(new ClearListAction(outputKitsList));
        moveKitAction = new MoveKitAction(Direction.INCREASE_INDEX, outputKitsList, tdInfo
                .getMaxNumberOfKits() - 1);
        addToButtonBar(moveKitAction);
        addToButtonBar(new MoveKitAction(Direction.DECREASE_INDEX, outputKitsList, 0));
        JComboBox modulesChooserCombo = new ModulesChooserCombo(tdInfo);
        modulesChooserCombo.addActionListener(this);
        addToButtonBar(modulesChooserCombo);
    }

    public void setMaxNumberOfKits(int maxNumberOfKits) {
        moveKitAction.setMaxNumberOfKits(maxNumberOfKits);
    }

    @SuppressWarnings("serial")
    private static class ModulesChooserCombo extends JComboBox {
        private ModulesChooserCombo(TdInfo defaultTdInfo) {
            Vector<TdInfo> availableModulesInfo = InfoManager.availableModulesInfo();
            for (TdInfo tdInfo : availableModulesInfo) {
                addItem(tdInfo);
            }
            setSelectedItem(defaultTdInfo);
            setFocusable(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        TdInfo tdInfo = (TdInfo) cb.getSelectedItem();
        moveKitAction.setMaxNumberOfKits(tdInfo.getMaxNumberOfKits() - 1);
        outputKitsList.setTdInfo(tdInfo);
    }
}
