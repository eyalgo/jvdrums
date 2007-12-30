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

package ui.lists;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;

import kits.TdKit;
import kits.info.TdInfo;
import ui.panels.KitsPanel;
import ui.swing.actions.BrowseAction;
import ui.swing.actions.ClearListAction;

/**
 * @author egolan
 */
public final class InputKitsList extends KitsList {
    private static final long serialVersionUID = -1312290418423601870L;
    private final DefaultListModel myModel;
    private final KitsPanel outputKistPanel;

    public InputKitsList(KitsPanel inputKitsPanel, KitsPanel outputKistPanel) {
        myModel = new DefaultListModel();
        this.setModel(myModel);
        this.setCellRenderer(new TdKitListRenderer());
        this.outputKistPanel = outputKistPanel;
        final JPopupMenu popup = new JPopupMenu();
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger())
                    popup.show(InputKitsList.this, e.getX(), e
                            .getY());
            }

            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 2) { // Double-click
                    // Get item index
                    int index = list.locationToIndex(evt.getPoint());
                    kitPressed(index);
                }

            }
        });
/*        addToButtonBar(new BrowseAction(getParentFrame(), this));
        addToButtonBar(new LoadFromModuleAction());
        addToButtonBar(new ClearListAction(inputKitsList));
        addToButtonBar(new MoveRightAction(inputKitsList, outputPanel));
*/
        popup.add(new BrowseAction(inputKitsPanel.getParentFrame(), inputKitsPanel, false));
        popup.add(new ClearListAction(this, false));
    }

    @SuppressWarnings("serial")
    private static class TdKitListRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean hasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index,
                    isSelected, hasFocus);
            if (value != null && value instanceof TdKit) {
                TdKit kit = (TdKit) value;
                label.setText(kit.getName());
            }
            // if (index%2 == 0) {
            // label.setBackground(Color.LIGHT_GRAY);
            // } else {
            // label.setBackground(Color.WHITE);
            // }
            return (label);
        }
    }

    @Override
    public void addKit(TdKit kit) {
        myModel.addElement(kit);
    }

    @Override
    public void addKits(TdKit[] kits) {
        for (TdKit kit : kits) {
            addKit(kit);
        }
    }

    void kitPressed(int index) {
        outputKistPanel.addKit((TdKit) myModel.elementAt(index));
    }

    @Override
    public void clear() {
        myModel.clear();
    }

    public TdKit[] getSelectedKits() {
        Object[] objs = getSelectedValues();
        TdKit[] selectedKits = new TdKit[objs.length];
        for (int i = 0; i < objs.length; i++) {
            selectedKits[i] = (TdKit) objs[i];
        }
        return selectedKits;
    }

    @Override
    public TdKit[] getKits() {
        Object[] objectKitsInList = myModel.toArray();
        TdKit[] kitsInList = new TdKit[objectKitsInList.length];
        for (int i = 0; i < objectKitsInList.length; i++) {
            kitsInList[i] = (TdKit) objectKitsInList[i];
        }
        return kitsInList;
    }

    @Override
    public int numberOfKits() {
        return myModel.getSize();
    }

    public void setTdInfo(TdInfo tdInfo) {
        // TODO Auto-generated method stub
        
    }
}
