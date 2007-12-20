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
import java.awt.event.ActionEvent;

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

import ui.panels.AboutPanel;
import ui.panels.KitPanelInput;
import ui.panels.KitPanelOutput;
import ui.panels.KitsPanel;
import ui.swing.BaseAction;
import ui.swing.Desktop;
import ui.utils.ExitListener;
import ui.utils.WindowUtilities;
import bias.Configuration;
import exceptions.VdrumException;

/**
 * @author Eyal Golan
 */
public final class MainFrame extends JFrame {
    private static final long serialVersionUID = -7164597771180443878L;
    private static Configuration config = Configuration.getRoot().get(MainFrame.class);
    private JButton connectButton;
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JMenu connectionMenu;
    private JMenuItem connectionMenuItem;
    private JMenuBar jMenuBar1;
    private JSplitPane jSplitPane1;
    private JToolBar mainToolBar;
    private KitsPanel inputPanel;
    private KitsPanel outputPanel;

    /** Creates new form MainFrame */
    public MainFrame() {
        super("JVDrums");
    }

    public void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new ExitListener());
        setMinimumSize(new Dimension(900, 600));
        setName("mainframe");
        WindowUtilities.setJavaLookAndFeel();
        mainToolBar = new JToolBar();
        connectButton = new JButton("Connect");
        jSplitPane1 = new JSplitPane();
        inputPanel = new KitPanelInput(this, (KitsPanel) (outputPanel = new KitPanelOutput(
                this)));
        jMenuBar1 = new JMenuBar();
        fileMenu = new JMenu("File");
        helpMenu = new JMenu("Help");
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

        fileMenu.add(new ExitAction());
        connectionMenu.add(connectionMenuItem);

        helpMenu.add(new WebsiteAction());
        helpMenu.add(new AboutAction());
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

    @SuppressWarnings("serial")
    private class ExitAction extends BaseAction {
        private ExitAction() {
            config.get("exit").read(this);
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            System.exit(0);
        }
    }


    @SuppressWarnings("serial")
    private class AboutAction extends BaseAction {
        private AboutAction() {
            config.get("about").read(this);
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            AboutPanel.showInDialog(MainFrame.this);
        }
    }

    /**
     * The action that shows the jvdrums website.
     */
    @SuppressWarnings("serial")
    private class WebsiteAction extends BaseAction {
        private WebsiteAction() {
            config.get("website").read(this);
            setEnabled(Desktop.isSupported());
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            Desktop.browse("http://jvdrums.sourceforge.net");
        }
    }
}
