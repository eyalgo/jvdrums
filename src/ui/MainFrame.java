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

package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * 
 * @author Eyal Golan
 */
public class MainFrame extends JFrame {
    private static final long serialVersionUID = -7164597771180443878L;

    /** Creates new form MainFrame */
    public MainFrame() {
        myInit();
        initComponents();
    }

    private void myInit() {
        String nativeLF = UIManager.getSystemLookAndFeelClassName();
        // Install the look and feel
        try {
            UIManager.setLookAndFeel(nativeLF);
        } catch (InstantiationException e) {
        } catch (ClassNotFoundException e) {
        } catch (UnsupportedLookAndFeelException e) {
        } catch (IllegalAccessException e) {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    @SuppressWarnings("serial")
    private void initComponents() {
        mainToolBar = new JToolBar();
        connectButton = new JButton();
        jSplitPane1 = new JSplitPane();
        inputPanel = new JPanel();
        inputButtonsPanel = new JPanel();
        inputLoadButton = new JButton();
        inputDownlaodButton = new JButton();
        outputPanel = new JPanel();
        outputButtonsPanel = new JPanel();
        outputSaveButton = new JButton();
        outputButtonUpload = new JButton();
        outputFunctionButtonsPanel = new JPanel();
        outputDeleteButton = new JButton();
        outputUpButton = new JButton();
        outputDownButton = new JButton();
        jMenuBar1 = new JMenuBar();
        fileMenu = new JMenu();
        exitMenuItem = new JMenuItem();
        connectionMenu = new JMenu();
        connectionMenuItem = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("JVDrums");
        setMinimumSize(new Dimension(600, 400));
        setName("mainframe"); // NOI18N

        mainToolBar.setFloatable(false);
        mainToolBar.setRollover(true);

        connectButton.setText("Connect");
        connectButton.setFocusable(false);
        connectButton.setHorizontalTextPosition(SwingConstants.CENTER);
        connectButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        mainToolBar.add(connectButton);

        getContentPane().add(mainToolBar, BorderLayout.NORTH);

        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setAlignmentX(0.5f);
        jSplitPane1.setAlignmentY(0.5f);
        jSplitPane1.setEnabled(false);

        inputPanel.setLayout(new BorderLayout());

        inputLoadButton.setText("Load");
        inputButtonsPanel.add(inputLoadButton);

        inputDownlaodButton.setText("Download");
        inputButtonsPanel.add(inputDownlaodButton);

        inputPanel.add(inputButtonsPanel, BorderLayout.SOUTH);

        jSplitPane1.setLeftComponent(inputPanel);

        outputPanel.setLayout(new BorderLayout());

        outputSaveButton.setText("Save");
        outputSaveButton.setFocusable(false);
        outputSaveButton.setHorizontalTextPosition(SwingConstants.CENTER);
        outputSaveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        outputButtonsPanel.add(outputSaveButton);

        outputButtonUpload.setText("Upload");
        outputButtonUpload.setFocusable(false);
        outputButtonUpload.setHorizontalTextPosition(SwingConstants.CENTER);
        outputButtonUpload.setVerticalTextPosition(SwingConstants.BOTTOM);
        outputButtonsPanel.add(outputButtonUpload);

        outputPanel.add(outputButtonsPanel, BorderLayout.SOUTH);

        outputFunctionButtonsPanel
                .setLayout(new FlowLayout(FlowLayout.LEFT));

        outputDeleteButton.setText("Delete");
        outputDeleteButton.setFocusable(false);
        outputDeleteButton.setHorizontalTextPosition(SwingConstants.CENTER);
        outputDeleteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        outputFunctionButtonsPanel.add(outputDeleteButton);

        outputUpButton.setText("Up");
        outputUpButton.setFocusable(false);
        outputUpButton.setHorizontalTextPosition(SwingConstants.CENTER);
        outputUpButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        outputFunctionButtonsPanel.add(outputUpButton);

        outputDownButton.setText("Down");
        outputDownButton.setFocusable(false);
        outputDownButton.setHorizontalTextPosition(SwingConstants.CENTER);
        outputDownButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        outputFunctionButtonsPanel.add(outputDownButton);

        outputPanel.add(outputFunctionButtonsPanel, BorderLayout.NORTH);

        jSplitPane1.setRightComponent(outputPanel);

        getContentPane().add(jSplitPane1, BorderLayout.CENTER);
        outputScrollPane = new JScrollPane();
        outputList = new JList();
        outputList.setModel(new AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        outputScrollPane.setViewportView(outputList);
        outputPanel.add(outputScrollPane, java.awt.BorderLayout.CENTER);
        
        inputScrollPane = new JScrollPane();
        inputList = new JList();
        inputList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        inputScrollPane.setViewportView(inputList);
        inputPanel.add(inputScrollPane, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);
        
        connectionMenu.setText("Connection");
        connectionMenuItem.setText("Connection");
        connectionMenu.add(connectionMenuItem);

        jMenuBar1.add(fileMenu);
        jMenuBar1.add(connectionMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(ActionEvent evt) {// GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }// GEN-LAST:event_exitMenuItemActionPerformed

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton connectButton;
    private JMenuItem exitMenuItem;
    private JMenu fileMenu;
    private JMenu connectionMenu;
    private JMenuItem connectionMenuItem;
    private JPanel inputButtonsPanel;
    private JButton inputDownlaodButton;
    private JButton inputLoadButton;
    private JPanel inputPanel;
    private JMenuBar jMenuBar1;
    private JSplitPane jSplitPane1;
    private JToolBar mainToolBar;
    private JButton outputButtonUpload;
    private JPanel outputButtonsPanel;
    private JButton outputDeleteButton;
    private JButton outputDownButton;
    private JPanel outputFunctionButtonsPanel;
    private JPanel outputPanel;
    private JButton outputSaveButton;
    private JButton outputUpButton;
    private JScrollPane outputScrollPane;
    private JScrollPane inputScrollPane;
    private JList outputList;
    private JList inputList;
    // End of variables declaration//GEN-END:variables
}
