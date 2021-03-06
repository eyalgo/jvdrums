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

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.ListDataListener;

import kits.TdKit;
import ui.MainFrame;
import ui.lists.KitsList;

public abstract class KitsPanel extends JPanel {
    private static final long serialVersionUID = -5323945831812498619L;
    private JScrollPane scrollPane;
    private JToolBar buttonsPanel;
    private final MainFrame parentFrame;
    private  KitsList kitList;

    public KitsPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        scrollPane = new JScrollPane();
        this.add(scrollPane, BorderLayout.CENTER);
        buttonsPanel = new JToolBar();
        buttonsPanel.setFloatable(false);
        this.add(buttonsPanel, BorderLayout.NORTH);
    }
    
//    public abstract KitsList getKitList();

    final void setListInPanel(KitsList kitList) {
        this.kitList = kitList;
        scrollPane.setViewportView(kitList);
    }
    
    public final MainFrame getParentFrame() {
        return this.parentFrame;
    }

    final void addToButtonBar(Component component) {
        buttonsPanel.add(component);
    }
    
    final void addToButtonBar(Action action) {
        buttonsPanel.add(action);
    }
    
    public final void addKit(TdKit kit) {
        kitList.addKit(kit);
    }

    public final void addKits(TdKit[] kits) {
        kitList.addKits(kits);
    }
    
    public final int numberOfKits() {
        return kitList.numberOfKits();
    }
    
    public TdKit[] getKits() {
        return kitList.getKits();
    }

    public void addListDataListener(ListDataListener l) {
        kitList.getModel().addListDataListener(l);
    }
}
