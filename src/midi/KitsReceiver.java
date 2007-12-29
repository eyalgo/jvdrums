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

import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

import org.apache.commons.lang.ArrayUtils;

import exceptions.VdrumException;

import managers.TDManager;
import managers.TDModulesManager;

import kits.TdKit;
import kits.info.TdInfo;

/**
 * @author Limor Eyal
 */
final class KitsReceiver implements Receiver {
    TdInfo tdInfo;
    byte[] receivedBytes = null;

    public KitsReceiver() {
        receivedBytes = null;
    }

    @Override
    public void close() {
    // TODO Auto-generated method stub

    }

    @Override
    public void send(MidiMessage midiMessage, long timeStamp) {
        // int numberOfSubParts = tdInfo.getNumberOfSubParts();

        // TDModulesManager modulesManager = tdInfo.getModulesManager();
        // try {
        // TdKit[] kits = modulesManager.sysexMessageToKits((SysexMessage)midiMessage);
        // for (TdKit kit : kits) {
        // System.out.println(kit);
        // }
        // }
        // catch (InvalidMidiDataException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // catch (VdrumException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // output.append("Received a MidiEvent:
        // "+Integer.toHexString(midiMessage.getStatus())+" Length: "+midiMessage.getLength()+"
        // at "+timeStamp+"\n");
        if (midiMessage instanceof SysexMessage) {
            // StringBuffer output = new StringBuffer();
            // output.append(" SysexMessage: " + (midiMessage.getStatus() - 256));
            byte[] message = ((SysexMessage) midiMessage).getMessage();
            // for (int x = 0; x < message.length; x++) {

            // for (int x=0;x<message.length;x++) output.append("
            // "+Integer.toHexString(message[x]));

            // System.out.println(output.toString());
            receivedBytes = ArrayUtils.addAll(receivedBytes, message);
            // System.out.println("receivedBytes length=" + receivedBytes.length);
            if (receivedBytes.length == tdInfo.getKitSize()) {

                try {
                    // System.out.println("BOOM");
                    TdKit kit = TDManager.bytesToOneKit(receivedBytes);
                    System.out.println(kit);
                    receivedBytes = null;
                }
                catch (InvalidMidiDataException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (VdrumException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            // if (receivedBytes.length == tdInfo.getKitSize()) {
            // try {
            // TdKit[] kits = TDManager.bytesToKits(receivedBytes);
            //                        
            // output.append(kits[0]);
            // // for (TdKit kit : kits) {
            // // output.append(kit);
            // //
            // // }
            // System.out.println(output.toString());
            // }
            // catch (InvalidMidiDataException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // catch (VdrumException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            //                    
            // receivedBytes = null;
            // }
            // output.append(" " + Integer.toHexString(data[x]));
            // }
            // System.out.println(output.toString());
        } else {
            System.out.println("Received a MidiEvent: "
                    + Integer.toHexString(midiMessage.getStatus()) + " Length: "
                    + midiMessage.getLength() + " at " + timeStamp + "\n");
        }

    }

    public void setTdIdInfo(TdInfo tdInfo) {
        this.tdInfo = tdInfo;
    }

}
