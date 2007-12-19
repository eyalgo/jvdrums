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
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import ui.panels.KitPanelInput;
import ui.panels.KitPanelOutput;
import ui.panels.KitsPanel;
import ui.utils.ExitListener;
import ui.utils.WindowUtilities;
import exceptions.VdrumException;

/**
 * @author Eyal Golan
 */
public final class MainFrame extends JFrame {
    private static final long serialVersionUID = -7164597771180443878L;
    private JButton connectButton;
    private JMenuItem exitMenuItem;
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JMenu connectionMenu;
    private JMenuItem connectionMenuItem;
    private JMenuItem aboutMenuItem;
    private JMenuBar jMenuBar1;
    private JSplitPane jSplitPane1;
    private JToolBar mainToolBar;
    private KitsPanel inputPanel;
    private KitsPanel outputPanel;

    /** Creates new form MainFrame */
    public MainFrame() {
        super("JVDrums");
        
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new ExitListener());
        setMinimumSize(new Dimension(900, 600));
        setName("mainframe");
        WindowUtilities.setJavaLookAndFeel();
        mainToolBar = new JToolBar();
        connectButton = new JButton("Connect");
        jSplitPane1 = new JSplitPane();
        inputPanel = new KitPanelInput(this,
                (KitsPanel) (outputPanel = new KitPanelOutput(this)));
        jMenuBar1 = new JMenuBar();
        fileMenu = new JMenu("File");
        helpMenu = new JMenu("Help");
        exitMenuItem = new JMenuItem("Exit");
        connectionMenu = new JMenu("Connection");
        connectionMenuItem = new JMenuItem("Connection");
        aboutMenuItem = new JMenuItem("About...");

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
        helpMenu.add(aboutMenuItem);
        jMenuBar1.add(fileMenu);
        jMenuBar1.add(connectionMenu);
        jMenuBar1.add(helpMenu);
        setJMenuBar(jMenuBar1);

        pack();
    }

    public void showErrorDialog(VdrumException vdrumException) {
        JOptionPane.showMessageDialog(this, vdrumException.getMessage(), vdrumException
                .getProblem(), JOptionPane.ERROR_MESSAGE);
    }

    public void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void exitMenuItemActionPerformed(ActionEvent evt) {
        System.exit(0);
    }

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
        final MainFrame mainFrame = new MainFrame();
        try {
            mainFrame.initFrame();
            mainFrame.setVisible(true);
        }
        catch (Error e) {
            mainFrame.showErrorDialog(e.getLocalizedMessage(), "Fatal Error");
            System.exit(-1);
        }
        catch (Exception e) {
            mainFrame.showErrorDialog(e.getLocalizedMessage(), "Fatal Exception");
            System.exit(-2);
        }
    }
}
