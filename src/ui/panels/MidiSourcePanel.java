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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;

import javax.sound.midi.MidiDevice;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jvdrums.UserPreferences;
import ui.MainFrame;
import ui.combobox.DestinationMidiComboBox;
import ui.combobox.MidiComboBox;
import ui.combobox.SourceMidiComboBox;
import ui.swing.StandardDialog;
import bias.Configuration;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class MidiSourcePanel extends StandardDialog {
    private final static int DEFAULT_DEVICE_ID = 16;
    private static Configuration config = Configuration.getRoot().get(MidiSourcePanel.class);
    MidiDevice.Info selectedDestinationMidiInfo;
    MidiDevice.Info selectedSourceMidiInfo;
    private final MainFrame mainFrame;
    private JPanel bodyPanel;
    private MidiComboBox sourceCombo = new SourceMidiComboBox();
    private MidiComboBox destinationCombo = new DestinationMidiComboBox();
    private JComboBox deviceIdCombo = new JComboBox();

    private MidiSourcePanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        config.read(this);
        initDialog();
    }

    private void initDialog() {
        bodyPanel = new JPanel();
        bodyPanel.setLayout(new GridBagLayout());
        /*
         * public GridBagConstraints () { gridx = RELATIVE; gridy = RELATIVE; gridwidth = 1;
         * gridheight = 1; weightx = 0; weighty = 0; anchor = CENTER; fill = NONE; insets = new
         * Insets(0, 0, 0, 0); ipadx = 0; ipady = 0; } public GridBagConstraints(int gridx, int
         * gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor,
         * int fill, Insets insets, int ipadx, int ipady) {
         */

        JLabel sourceLabel = new JLabel();
        config.get("sourceLabel").read(sourceLabel);
        bodyPanel.add(sourceLabel,
                new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        GridBagConstraints gridBagConstraints;

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        bodyPanel.add(sourceCombo, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        JLabel destinationLabel = new JLabel();
        config.get("destinationLabel").read(destinationLabel);
        bodyPanel.add(destinationLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        bodyPanel.add(destinationCombo, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        JLabel deviceIdLabel = new JLabel();
        config.get("deviceIdLabel").read(deviceIdLabel);
        bodyPanel.add(deviceIdLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        bodyPanel.add(deviceIdCombo, gridBagConstraints);

        for (int i = 1; i < 33; i++) {
            deviceIdCombo.addItem(new Integer(i));
        }
        deviceIdCombo.setSelectedIndex(DEFAULT_DEVICE_ID);

        destinationCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                destinationItemStateChanged(evt);
            }
        });

        sourceCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sourceItemStateChanged(evt);
            }
        });

        setResizable(false);
        setBounds(null);

        addOKAction();
        addCancelAction();
        setBody(bodyPanel);

        initSelected();
    }

    private void initSelected() {
        destinationCombo.setSelectedItem(UserPreferences.getInstance().get("destinationCombo",
                "None"));
        sourceCombo.setSelectedItem(UserPreferences.getInstance().get("sourceCombo", "None"));

        deviceIdCombo.setSelectedIndex(UserPreferences.getInstance().getInt("deviceIdCombo",
                DEFAULT_DEVICE_ID));

    }

    private void destinationItemStateChanged(ItemEvent evt) {
        switch (evt.getStateChange()) {
            case ItemEvent.SELECTED:
                int selectedIndex = ((JComboBox) evt.getSource()).getSelectedIndex();
                if (selectedIndex != 0) {
                    selectedDestinationMidiInfo = (MidiDevice.Info) ((JComboBox) evt
                            .getSource()).getItemAt(selectedIndex);
                } else {
                    selectedDestinationMidiInfo = null;
                }
                break;
            case ItemEvent.DESELECTED:
                break;
        }
    }

    private void sourceItemStateChanged(ItemEvent evt) {
        switch (evt.getStateChange()) {
            case ItemEvent.SELECTED:
                int selectedIndex = ((JComboBox) evt.getSource()).getSelectedIndex();
                if (selectedIndex != 0) {
                    selectedSourceMidiInfo = (MidiDevice.Info) ((JComboBox) evt.getSource())
                            .getItemAt(selectedIndex);
                } else {
                    selectedSourceMidiInfo = null;
                }
                break;
            case ItemEvent.DESELECTED:
                break;
        }
    }

    @Override
    public void onOK() {
        UserPreferences.getInstance().put("sourceCombo",
                sourceCombo.getSelectedItem().toString());
        UserPreferences.getInstance().put("destinationCombo",
                destinationCombo.getSelectedItem().toString());
        UserPreferences.getInstance()
                .putInt("deviceIdCombo", deviceIdCombo.getSelectedIndex());
        mainFrame.setDestinationDeviceInformation(selectedDestinationMidiInfo,
                selectedSourceMidiInfo, deviceIdCombo.getSelectedIndex());
        super.onOK();
    }

    public static void showDialog(MainFrame mainFrame, MidiDevice.Info destinationMidiInfo) {
        final MidiSourcePanel midiSourcePanel = new MidiSourcePanel(mainFrame);
        midiSourcePanel.setVisible(true);
        midiSourcePanel.dispose();
    }
}
