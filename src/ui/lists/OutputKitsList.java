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

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;

import kits.TdKit;
import ui.panels.KitsPanel;
import utils.VDrumsUtils;

/**
 * @author egolan
 */
public final class OutputKitsList extends KitsList {
    public enum Direction {
        DECREASE_INDEX("up"), INCREASE_INDEX("down");
        private final String name;

        Direction(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = 7560978682051239230L;
    private final OutputListModel myModel;

    public OutputKitsList(KitsPanel outputKistPanel) {
        myModel = new OutputListModel();
        this.setModel(myModel);
        this.setCellRenderer(new TdKitListRenderer());
        setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
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
                    popup.show(OutputKitsList.this, e.getX(), e.getY());
            }
        });

        /*
         addToButtonBar(new SaveAction(getParentFrame(), this));
        addToButtonBar(new SendToModuleAction(getParentFrame(), outputKitsList));
        addToButtonBar(new RemoveKitsAction(outputKitsList));
        addToButtonBar(new ClearListAction(outputKitsList));
        addToButtonBar(new MoveKitAction(Direction.INCREASE_INDEX, outputKitsList,
                VDrumsUtils.MAX_NUMBER_OF_KITS - 1));
        addToButtonBar(new MoveKitAction(Direction.DECREASE_INDEX, outputKitsList, 0));
         */
//        popup.add(new SaveAction(outputKistPanel.getParentFrame(), outputKistPanel));
    }

    @Override
    public void addKit(TdKit kit) {
        int index = myModel.addKit(kit, getSelectedIndex());
        increaseIndex(index);
    }

    public void addKits(TdKit[] kits) {
        if (kits.length < 1) {
            return;
        }
        int index = getSelectedIndex();
        if (index == -1) {
            index = myModel.getFirstIndex();
        }
        for (TdKit kit : kits) {
            index = myModel.addKit(kit, index);
            increaseIndex(index);
            index = getSelectedIndex();
        }
    }

    @Override
    public void clear() {
        myModel.clear();
    }

    @Override
    public final TdKit[] getKits() {
        return myModel.getKits();
    }

    @SuppressWarnings("serial")
    private static class TdKitListRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean hasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index,
                    isSelected, hasFocus);
            if (value == null) {
                label.setText(index + 1 + " -> ");
            }
            if (value != null && value instanceof TdKit) {
                TdKit kit = (TdKit) value;
                label.setText(index + 1 + " -> " + kit.getName());
            }
            // if (index % 2 == 0) {
            // label.setBackground(Color.MAGENTA);
            // } else {
            // label.setBackground(Color.WHITE);
            // }
            return (label);
        }
    }

    @Override
    public int numberOfKits() {
        return myModel.numberOfKits();
    }

    private void increaseIndex(int index) {
        int selectedIndex = index;
        selectedIndex++;
        if (selectedIndex < VDrumsUtils.MAX_NUMBER_OF_TD12_KITS) {
            setSelectedIndex(selectedIndex);
        } else {
            setSelectedIndex(-1);
        }
    }

    public void moveSelection(final Direction direction) {
        final int selectedRow = this.getSelectedIndex();
        if (selectedRow != -1) {
            int newIndex = myModel.moveSelection(selectedRow, direction);
            if (newIndex != -1) {
                setSelectedIndex(newIndex);
            }
        }
    }

    public void deleteSelectedKit() {
        final int selectedRow = this.getSelectedIndex();
        if (selectedRow != -1) {
            myModel.removeSelectedKit(selectedRow);
        }
    }

    @SuppressWarnings("serial")
    private class OutputListModel extends AbstractListModel {
        private int numberOfKits;
        private final TdKit[] kits;

        private OutputListModel() {
            kits = new TdKit[VDrumsUtils.MAX_NUMBER_OF_TD12_KITS];
            clear();
        }

        private void removeSelectedKit(int selectedRow) {
            try {
                kits[selectedRow] = null;
                numberOfKits--;
                fireContentsChanged(this, selectedRow, selectedRow);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        private int moveSelection(final int currentSelection, final Direction direction) {
            int newIndex = getNewIndex(currentSelection, direction);
            if (newIndex == -1) {
                return -1;
            }
            try {
                TdKit otherKit = kits[newIndex];
                TdKit selectedKit = kits[currentSelection];
                kits[newIndex] = selectedKit;
                kits[currentSelection] = otherKit;
                fireContentsChanged(this, currentSelection, newIndex);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                return -1;
            }
            return newIndex;
        }

        private int getNewIndex(final int currentSelection, final Direction direction) {
            int newIndex = -1;
            switch (direction) {
                case DECREASE_INDEX:
                    newIndex = currentSelection - 1;
                    break;
                case INCREASE_INDEX:
                    newIndex = currentSelection + 1;
                    break;
            }
            return newIndex;
        }

        private int numberOfKits() {
            return numberOfKits;
        }

        private TdKit[] getKits() {
            return kits;
        }

        private int addKit(TdKit kit, int index) {
            if (index < 0) {
                index = getFirstIndex();
            }
            if (index == -1) {
                return -1;
            }
            kits[index] = kit;
            numberOfKits++;
            fireContentsChanged(this, index, index);
            return index;
        }

        private int getFirstIndex() {
            for (int i = 0; i < kits.length; i++) {
                if (kits[i] == null) {
                    return i;
                }
            }
            return -1;
        }

        private void clear() {
            for (int i = 0; i < kits.length; i++) {
                kits[i] = null;
            }
            numberOfKits = 0;
            fireContentsChanged(this, 0, kits.length);
        }

        @Override
        public Object getElementAt(int index) {
            return kits[index];
        }

        @Override
        public int getSize() {
            return kits.length;
        }
    }
}
