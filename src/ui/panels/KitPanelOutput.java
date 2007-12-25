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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kits.TdKit;
import ui.MainFrame;
import ui.lists.OutputKitsList;
import ui.lists.OutputKitsList.Direction;
import ui.swing.actions.SaveAction;
import ui.swing.actions.SendToModuleAction;
import utils.VDrumsUtils;

/**
 * @author egolan
 */
public final class KitPanelOutput extends KitsPanel {
    private static final long serialVersionUID = -5217989811338648085L;
    private JButton removeKitButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private JButton clearButton;

    public KitPanelOutput(MainFrame parentFrame) {
        super(parentFrame, new OutputKitsList());
        
        removeKitButton = new JButton(removeFromList());
        removeKitButton.setToolTipText("Remove from list");
        
        moveDownButton = new OutputButton("Move down", "down-32x32.png",
                Direction.DECREASE_INDEX, VDrumsUtils.MAX_NUMBER_OF_KITS - 1);
        moveUpButton = new OutputButton("Move up", "up-32x32.png", Direction.INCREASE_INDEX, 0);
        
        clearButton = new JButton(clearList());
        clearButton.setToolTipText("Clear list");
        
        addToButtonBar(new SaveAction(getParentFrame(), (OutputKitsList) getKitList()));
        
        addToButtonBar(new SendToModuleAction(getParentFrame(),(OutputKitsList) getKitList()));
        addToButtonBar(removeKitButton);
        addToButtonBar(clearButton);
        addToButtonBar(moveUpButton);
        addToButtonBar(moveDownButton);
        moveDownButton.setEnabled(false);
        moveUpButton.setEnabled(false);
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
