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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import kits.TdKit;
import ui.panels.KitPanelInput;
import ui.panels.KitPanelOutput;
import ui.panels.KitsPanel;
import ui.utils.ExitListener;
import ui.utils.WindowUtilities;

/**
 * @author Eyal Golan
 */
public final class MainFrame extends JFrame {
    private static final long serialVersionUID = -7164597771180443878L;
    private JButton connectButton;
    private JMenuItem exitMenuItem;
    private JMenu fileMenu;
    private JMenu connectionMenu;
    private JMenuItem connectionMenuItem;
    private JMenuBar jMenuBar1;
    private JSplitPane jSplitPane1;
    private JToolBar mainToolBar;
    private KitsPanel inputPanel;
    private KitsPanel outputPanel;
    private JTextArea text;

    /** Creates new form MainFrame */
    public MainFrame() {
        super("JVDrums");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new ExitListener());
        setMinimumSize(new Dimension(900, 600));
        setName("mainframe");
        WindowUtilities.setNativeLookAndFeel();
        mainToolBar = new JToolBar();
        connectButton = new JButton("Connect");
        jSplitPane1 = new JSplitPane();
        inputPanel = new KitPanelInput(this);
        outputPanel = new KitPanelOutput(this);
        jMenuBar1 = new JMenuBar();
        fileMenu = new JMenu("File");
        exitMenuItem = new JMenuItem("Exit");
        connectionMenu = new JMenu("Connection");
        connectionMenuItem = new JMenuItem("Connection");

        mainToolBar.setFloatable(false);
        mainToolBar.setRollover(true);

        connectButton.setFocusable(false);
        connectButton.setHorizontalTextPosition(SwingConstants.CENTER);
        connectButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        mainToolBar.add(connectButton);

        getContentPane().add(mainToolBar, BorderLayout.NORTH);

        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setAlignmentX(0.5f);
        jSplitPane1.setAlignmentY(0.5f);
        jSplitPane1.setEnabled(false);

        jSplitPane1.setLeftComponent(inputPanel);

        jSplitPane1.setRightComponent(outputPanel);

        getContentPane().add(jSplitPane1, BorderLayout.CENTER);

        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);
        connectionMenu.add(connectionMenuItem);
        jMenuBar1.add(fileMenu);
        jMenuBar1.add(connectionMenu);
        setJMenuBar(jMenuBar1);
        
        text = new JTextArea();
        text.setAutoscrolls(true);
        text.setLineWrap(true);
        text.setRows(10);
        getContentPane().add(text, BorderLayout.SOUTH);
        pack();
    }
    
    public void problem (String o) {
        text.append(o);
        text.append(System.getProperty("line.separator"));
    }

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
                createAndShowGui();
            }
        });
    }

    private static void createAndShowGui() {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
    
    public void leftKitPressed(TdKit kit) {
        outputPanel.addKit(kit);
    }
}
