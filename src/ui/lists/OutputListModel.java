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

import javax.swing.AbstractListModel;

import kits.TdKit;
import kits.info.TdInfo;
import ui.lists.OutputKitsList.Direction;

@SuppressWarnings("serial")
class OutputListModel extends AbstractListModel {
    private TdInfo tdInfo;
    private int numberOfKits;
    private TdKit[] kits;

    OutputListModel() {
    }
    
    void setTdInfo(TdInfo tdInfo) {
        this.tdInfo = tdInfo;
        kits = new TdKit[this.tdInfo.getMaxNumberOfKits()];
        clear();
    }

    void removeSelectedKit(int selectedRow) {
        try {
            kits[selectedRow] = null;
            numberOfKits--;
            fireContentsChanged(this, selectedRow, selectedRow);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    int moveSelection(final int currentSelection, final Direction direction) {
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

    int numberOfKits() {
        return numberOfKits;
    }

    TdKit[] getKits() {
        return kits;
    }

    int addKit(TdKit kit, int index) {
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

    int getFirstIndex() {
        for (int i = 0; i < kits.length; i++) {
            if (kits[i] == null) {
                return i;
            }
        }
        return -1;
    }

    void clear() {
//        if (kits == null) {
//            return;
//        }
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
