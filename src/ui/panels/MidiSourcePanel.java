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
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.MainFrame;
import ui.swing.StandardDialog;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class MidiSourcePanel extends StandardDialog {
    MidiDevice destinationDevice;
    Transmitter actTransmitter = null;

    MidiDevice.Info mdi[];
    Vector<MidiDevice.Info> destinationInfoVector;

    private final MainFrame mainFrame;
    private JPanel bodyPanel;
    private JLabel sourceLabel = new JLabel("Source");
    private JComboBox sourceCombo = new JComboBox();
    private JLabel destinationLabel = new JLabel("Destination");
    private JComboBox destinationCombo = new JComboBox();

    private MidiSourcePanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        initDialog();
    }

    private void initDialog() {
        setTitle("MIDI Source");
        // setDescription("Choose MIDI Source");
        bodyPanel = new JPanel();
        // bodyPanel.setMinimumSize(new Dimension(300, 150));
        bodyPanel.setLayout(new GridBagLayout());
        /*
         * public GridBagConstraints () { gridx = RELATIVE; gridy = RELATIVE; gridwidth = 1;
         * gridheight = 1; weightx = 0; weighty = 0; anchor = CENTER; fill = NONE; insets = new
         * Insets(0, 0, 0, 0); ipadx = 0; ipady = 0; } public GridBagConstraints(int gridx, int
         * gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor,
         * int fill, Insets insets, int ipadx, int ipady) {
         */

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
        bodyPanel.add(destinationLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        bodyPanel.add(destinationCombo, gridBagConstraints);

        sourceCombo.addItem("None");
        destinationCombo.addItem("None");

        destinationInfoVector = new Vector<MidiDevice.Info>();
        destinationCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                destinationItemStateChanged(evt);
            }
        });

        initCombos();

        setResizable(false);
        setBounds(null);

        addOKAction();
        addCancelAction();
        setBody(bodyPanel);
    }

    private void initCombos() {
        mdi = MidiSystem.getMidiDeviceInfo();
        MidiDevice md;
        for (int i = 0; i < mdi.length; i++) {
            try {
                md = MidiSystem.getMidiDevice(mdi[i]);

                if (md.getMaxReceivers() != 0) {
                    destinationInfoVector.add(mdi[i]);
                    destinationCombo.addItem(mdi[i].getName());
                }

                if (md.getMaxTransmitters() != 0) {
                    sourceCombo.addItem(mdi[i].getName());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void destinationItemStateChanged(ItemEvent evt) {
        switch (evt.getStateChange()) {
            case ItemEvent.SELECTED:
                int oldDestination = ((JComboBox) evt.getSource()).getSelectedIndex();
                if (oldDestination != 0) {
                    try {
                        destinationDevice = MidiSystem
                                .getMidiDevice((MidiDevice.Info) destinationInfoVector
                                        .get(oldDestination - 1));
                    }
                    catch (MidiUnavailableException e) {
                        System.out.println("Port already in use");
                        // e.printStackTrace ();
                    }
                } else {
                    destinationDevice = null;
                }
                if (actTransmitter != null) {
                }
                break;
            case ItemEvent.DESELECTED:
                // System.out.println ("Destination deselektiert: "+oldDestination);
//                if (actReceiver != null) {
//                    actReceiver.close();
//                }
                break;
        }

    }

    @Override
    public void onOK() {
        mainFrame.setDestinationDevice(destinationDevice);
        super.onOK();
    }

    public static void showDialog(MainFrame mainFrame) {
        final MidiSourcePanel midiSourcePanel = new MidiSourcePanel(mainFrame);
        midiSourcePanel.setVisible(true);
        midiSourcePanel.dispose();
    }
}
