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
import javax.swing.SwingUtilities;
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
final class KitsReceiver implements Receiver {
    private static Configuration config = Configuration.getRoot().get(KitsReceiver.class);
    private final static int EXPIRE_TIME = 2500;
    private TdInfo tdInfo;
    private byte[] receivedBytes = null;
    private KitPanelInput inputPanel;
    private final MainFrame mainFrame;
    private String receivingMessage;
    private String finishedReceivingMessage;
    private final List<TdKit> receivedKits;
    private final Timer timer;
    private boolean messageSent;
    private final ActionListener timerActionListener;

    KitsReceiver(MainFrame mainFrame) {
        receivedBytes = null;
        this.mainFrame = mainFrame;
        receivedKits = new ArrayList<TdKit>();
        timerActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerExpired();
            }
        };
        timer = new Timer(EXPIRE_TIME, timerActionListener);
        timer.setRepeats(false);
        timer.setCoalesce(true);
        messageSent = false;
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
            if (isFirstPart) {
                timer.restart();
                receivedBytes = null;
                if (!messageSent) {
                    messageSent = true;
                    setMessage(receivingMessage, Color.GREEN);
                }
            }
            receivedBytes = ArrayUtils.addAll(receivedBytes, message);
            if (isLastPart) {
                try {
                    final TdKit kit = TDManager.bytesToOneKit(receivedBytes);
                    // strBuilder.append(finishedReceivingMessage).append("
                    // ").append(kit.getName());
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
            }
        }
    }

    private void setMessage(final String message, final Color color) {
        if (SwingUtilities.isEventDispatchThread()) {
            mainFrame.putTextInStatusBar(message, color);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    mainFrame.putTextInStatusBar(message, color);
                }
            });
        }
    }

    void setTdIdInfo(TdInfo tdInfo) {
        this.tdInfo = tdInfo;
        receivedKits.clear();
    }

    void setKitPanelInput(KitPanelInput inputPanel) {
        this.inputPanel = inputPanel;
    }

    /**
     * Happens when the timer expires
     * 
     * @param e
     *            ActionEvent
     */
    private void timerExpired() {
        timer.stop();
        inputPanel.addKits(receivedKits);
        for (TdKit kit : receivedKits) {
            System.out.println(kit);
        }
        messageSent = false;
        setMessage(finishedReceivingMessage, Color.BLUE);
        receivedKits.clear();
    }
}
