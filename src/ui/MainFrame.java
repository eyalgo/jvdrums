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
import java.awt.Color;
import java.awt.Dimension;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

import kits.info.Td6Info;
import midi.MidiHandler;
import ui.event.ConnectionListener;
import ui.panels.KitPanelInput;
import ui.panels.KitPanelOutput;
import ui.swing.MultiLineLabel;
import ui.swing.actions.AboutAction;
import ui.swing.actions.BrowseAction;
import ui.swing.actions.ExitAction;
import ui.swing.actions.MidiSourceAction;
import ui.swing.actions.SaveAction;
import ui.swing.actions.WebsiteAction;
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
    private final MidiHandler midiHandler;
    private final JTextArea infoText;
    private final StatusBar statusBar;
    KitPanelOutput outputPanel;
    KitPanelInput inputPanel;

    public MainFrame() {
        super();
        config.read(this);
        midiHandler = new MidiHandler();
        infoText = new MultiLineLabel(10, 70);
        infoText.setBackground(new JTextArea().getBackground());
        statusBar = new StatusBar();
    }

    public void initFrame() {
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new ExitListener());
        setMinimumSize(new Dimension(800, 700));
        setName("mainframe");
        WindowUtilities.setLiquidLookAndFeel();
        JSplitPane jSplitPane1 = new JSplitPane();
        outputPanel = new KitPanelOutput(this, new Td6Info());
        inputPanel = new KitPanelInput(this, outputPanel);
        JMenuBar jMenuBar1 = new JMenuBar();
        JMenu fileMenu = new JMenu();
        config.get("fileMenu").read(fileMenu);
        JMenu helpMenu = new JMenu();
        config.get("helpMenu").read(helpMenu);
        JMenu configurationMenu = new JMenu();
        config.get("configurationMenu").read(configurationMenu);
        JMenu editMenu = new JMenu();
        config.get("editMenu").read(editMenu);

        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setAlignmentX(0.5f);
        jSplitPane1.setAlignmentY(0.5f);
        jSplitPane1.setEnabled(false);

        jSplitPane1.setLeftComponent(inputPanel);

        jSplitPane1.setRightComponent(outputPanel);

        getContentPane().add(jSplitPane1, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.add(new JScrollPane(infoText), BorderLayout.CENTER);
        infoPanel.add(statusBar, BorderLayout.SOUTH);

        getContentPane().add(infoPanel, BorderLayout.SOUTH);

        fileMenu.add(new BrowseAction(this, inputPanel, false));
        fileMenu.add(new SaveAction(this, outputPanel, false));
        fileMenu.addSeparator();
        fileMenu.add(new ExitAction());
        configurationMenu.add(new MidiSourceAction(this));

        helpMenu.add(new WebsiteAction());
        helpMenu.add(new AboutAction(this));
        jMenuBar1.add(fileMenu);
        jMenuBar1.add(configurationMenu);
        jMenuBar1.add(editMenu);
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

    public void setDestinationDeviceInformation(final MidiDevice.Info destinationDevice,
            final MidiDevice.Info sourceDevice, int deviceId) {
        try {
            midiHandler.setSourceAndDestination(sourceDevice, destinationDevice, deviceId);
        }
        catch (MidiUnavailableException e) {
            showErrorDialog("MDestination MIDI Problem", "Problem opening port");
        }
    }

    public void addInfo(final String newInfo) {
        infoText.append(newInfo);
        infoText.append(System.getProperty("line.separator"));
    }

    public void operationStart(String message, Color color) {
        setEnabled(false);
        statusBar.setForeground(color);
        statusBar.setText(message);
    }

    public void operationFinish() {
        statusBar.setText(" ");
        statusBar.setForeground(new JLabel().getForeground());
        setEnabled(true);
    }

    @SuppressWarnings("serial")
    private class StatusBar extends JLabel {
        private StatusBar() {
            super();
            setBorder(new BevelBorder(BevelBorder.LOWERED));
            setMessage(" ");
        }

        public void setMessage(String message) {
            setText(" " + message);
        }
    }

    public void addConnectionListener(ConnectionListener connectionListener) {
        midiHandler.addConnectionListener(connectionListener);
    }
    
    public MidiHandler getMidiHandler() {
        return this.midiHandler;
    }
}
