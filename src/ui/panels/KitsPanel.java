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

import ui.MainFrame;
import ui.lists.KitsList;

public abstract class KitsPanel extends JPanel {
    private static final long serialVersionUID = -5323945831812498619L;
    private JScrollPane scrollPane;
//    private KitsList kitList;
    private JToolBar buttonsPanel;
    private final MainFrame parentFrame;

    public KitsPanel(MainFrame parentFrame/*, KitsList kitList*/) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
//        kitList = kistList;
        scrollPane = new JScrollPane();
//        scrollPane.setViewportView(kitList);
        this.add(scrollPane, BorderLayout.CENTER);
        buttonsPanel = new JToolBar();
        this.add(buttonsPanel, BorderLayout.SOUTH);
    }
    
    public abstract KitsList getKitList();
    
//    public final KitsList getKitList() {
//        return kitList;
//    }

    final void setListInView(KitsList kitList) {
        scrollPane.setViewportView(kitList);
    }
    
    final MainFrame getParentFrame() {
        return this.parentFrame;
    }

    final void addToButtonBar(Component component) {
        buttonsPanel.add(component);
    }
    
    final void addToButtonBar(Action action) {
        buttonsPanel.add(action);
    }
}
