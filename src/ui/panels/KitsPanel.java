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
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import ui.MainFrame;
import ui.lists.KitsList;

public abstract class KitsPanel extends JPanel {
    private static final long serialVersionUID = -5323945831812498619L;
    private JScrollPane scrollPane;
    private KitsList kitList;
    private JPanel buttonsPanel;
    private final MainFrame parentFrame;

    public KitsPanel(MainFrame parentFrame, KitsList kistList) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        kitList = kistList;
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(kitList);
        this.add(scrollPane, BorderLayout.CENTER);
        buttonsPanel = new JPanel();
        this.add(buttonsPanel, BorderLayout.SOUTH);
//        kitList.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent evt) {
//                JList list = (JList) evt.getSource();
//                if (evt.getClickCount() == 2) { // Double-click
//                    // Get item index
//                    int index = list.locationToIndex(evt.getPoint());
//                    kitPressed(index);
//                }
//            }
//        });
//        kitList.setSelectionForeground(Color.BLACK);
////        kitList.setSelectionBackground(Color.BLUE);
//        kitList.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    final protected JFileChooser createFileChooser(String name) {
        JFileChooser fc = new JFileChooser();
        // fc.setDialogTitle(getResourceMap().getString(name + ".dialogTitle"));
        // String textFilesDesc = getResourceMap().getString("txtFileExtensionDescription");
//        fc.setDialogTitle("Download Syx files");
        fc.addChoosableFileFilter(new SyxFileFilter());
        fc.setAcceptAllFileFilterUsed(false);
        // fc.putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
        return fc;
    }
    
    final KitsList getKitList() {
        return kitList;
    }

    final MainFrame getParentFrame() {
        return this.parentFrame;
    }

    final void addToButtonBar(Component component) {
        buttonsPanel.add(component);
    }

    
//    protected final TdKit getKitAt(int index) {
//        return (TdKit)kitListModel.elementAt(index);
//    }
//    
//    protected final int numberOfKits() {
//        return kitListModel.getSize();
//    }
    
//    protected final TdKit[] getKits() {
//        Object[] objectKitsInList = kitListModel.toArray();
//        TdKit[] kitsInList = new TdKit[objectKitsInList.length];
//        for (int i = 0; i < objectKitsInList.length; i++) {
//            kitsInList[i] = (TdKit) objectKitsInList[i];
//        }
//        return kitsInList;
//    }

    @SuppressWarnings("serial")
    protected Action clearList() {
        final Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                getKitList().clear();
            }
        };
       return action; 
    }


    /**
     * This is a substitute for FileNameExtensionFilter, which is only available on Java SE 6.
     */
    private static class SyxFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String filename = f.getName();
            return filename.endsWith(".syx");
        }

        @Override
        public String getDescription() {
            return "*.syx";
        }
    }
}
