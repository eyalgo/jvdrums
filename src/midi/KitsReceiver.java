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

package midi;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;
import javax.swing.Timer;

import kits.TdKit;
import kits.info.TdInfo;
import managers.TDManager;

import org.apache.commons.lang.ArrayUtils;

import ui.MainFrame;
import ui.panels.KitPanelInput;
import bias.Configuration;
import exceptions.VdrumException;

/**
 * @author Limor Eyal
 */
final class KitsReceiver implements Receiver, ActionListener {
    private static Configuration config = Configuration.getRoot().get(KitsReceiver.class);
    private TdInfo tdInfo;
    private byte[] receivedBytes = null;
    private KitPanelInput inputPanel;
    private final MainFrame mainFrame;
    private String receivingMessage;
    private String kitReceivedMessage;
    private final List<TdKit> receivedKits;
    Timer timer;

    KitsReceiver(MainFrame mainFrame) {
        receivedBytes = null;
        this.mainFrame = mainFrame;
        receivedKits = new ArrayList<TdKit>();
        timer = new Timer(2500, this);
        timer.setRepeats(false);
        timer.setCoalesce(true);
        config.read(this);
    }

    @Override
    public void close() {}

    @Override
    public void send(MidiMessage midiMessage, long timeStamp) {
        if (midiMessage instanceof SysexMessage) {
            byte[] message = ((SysexMessage) midiMessage).getMessage();
            boolean isFirstPart = ((message[tdInfo.getSubPartIndex()] & 0xFF) == 0);
            boolean isLastPart = ((message[tdInfo.getSubPartIndex()] & 0xFF) == tdInfo
                    .getNumberOfSubParts() - 1);
            Color color = Color.GREEN;

            if (isLastPart) {
                color = Color.BLUE;
            }
            if (isFirstPart) {
                timer.restart();
                receivedBytes = null;
            }
            receivedBytes = ArrayUtils.addAll(receivedBytes, message);
            StringBuilder strBuilder = new StringBuilder();
            if (isLastPart) {
                try {
                    final TdKit kit = TDManager.bytesToOneKit(receivedBytes);
                    strBuilder.append(kitReceivedMessage).append(" ").append(kit.getName());
                    // addToPanel(kit, strBuilder.toString());
                    // System.out.println(kit + " " + timeStamp );// total 1526000 micro
                    // mainFrame.putTextInStatusBar(strBuilder.toString(), color);
                    receivedKits.add(kit);
                    receivedBytes = null;
                }
                catch (InvalidMidiDataException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (VdrumException e) {
                    // TODO Auto-generated catch block
                    // Sometimes message length is bad.
                    // I need to inform somehow the user.
                    e.printStackTrace();
                }
            } else {
                strBuilder.append(receivingMessage).append(" ");
                int partIndex = message[tdInfo.getSubPartIndex()] & 0xFF;
                partIndex++;
                strBuilder.append(partIndex);
                strBuilder.append(" ").append(timeStamp);
                setMessage(strBuilder.toString(), color);
                // mainFrame.putTextInStatusBar(strBuilder.toString(), color);
            }
            // receivedBytes = ArrayUtils.addAll(receivedBytes, message);
            // System.out.println(receivedBytes.length);
            // if (receivedBytes.length == tdInfo.getKitSize()) {
            // try {
            // final TdKit kit = TDManager.bytesToOneKit(receivedBytes);
            // strBuilder.append(kitReceivedMessage).append(" ").append(kit.getName());
            // // mainFrame.putTextInStatusBar(strBuilder.toString(), color);
            // addToPanel(kit, strBuilder.toString());
            // receivedBytes = null;
            // }
            // catch (InvalidMidiDataException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // catch (VdrumException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // }
        } else {
            // System.out.println("Received a MidiEvent:
            // "+Integer.toHexString(midiMessage.getStatus())+" Length:
            // "+midiMessage.getLength());
        }
    }

    private void setMessage(final String message, final Color color) {
//        System.out.println(message);
        // if (SwingUtilities.isEventDispatchThread()) {
        // mainFrame.putTextInStatusBar(message, color);
        // } else {
        // SwingUtilities.invokeLater(new Runnable() {
        // public void run() {
        // mainFrame.putTextInStatusBar(message, color);
        // }
        // });
        // }
    }

    // private void addToPanel(final TdKit kit, final String message) {
    // if (SwingUtilities.isEventDispatchThread()) {
    // setMessage(message, Color.BLUE);
    // // inputPanel.addKit(kit);
    // } else {
    // SwingUtilities.invokeLater(new Runnable() {
    // public void run() {
    // setMessage(message, Color.BLUE);
    // // inputPanel.addKit(kit);
    // }
    // });
    // }
    // }

    void setTdIdInfo(TdInfo tdInfo) {
        this.tdInfo = tdInfo;
        receivedKits.clear();
    }

    void setKitPanelInput(KitPanelInput inputPanel) {
        this.inputPanel = inputPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.stop();
        inputPanel.addKits(receivedKits);
        for (TdKit kit : receivedKits) {
            System.out.println(kit);
        }
        receivedKits.clear();
    }
}
