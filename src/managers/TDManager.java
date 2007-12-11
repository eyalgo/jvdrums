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

package managers;

import java.util.ArrayList;
import java.util.Collection;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import kits.TdKit;
import midi.VdrumsSysexMessage;

import org.apache.commons.lang.ArrayUtils;

import utils.VDrumsUtils;
import exceptions.VdrumException;

/**
 * @author egolan
 * 
 */
public final class TDManager {
    final TdKit[] tdKits;
    public TDManager() {
        super();
        this.tdKits = new TdKit[VDrumsUtils.MAX_NUMBER_OF_KITS];
        for (int i = 0; i < tdKits.length; i++) {
            tdKits[i] = null;
        }
    }

    /**
     * 
     * 
     * 
     * @param kits TdKit[]
     * @throws InvalidMidiDataException
     */
    public SysexMessage kitsToSysexMessage(TdKit[] kits) throws InvalidMidiDataException {
        byte[] data = null;
        for (int i = 0; i < kits.length; i++) {
            if (kits[i] != null) {
                TdKit tempKit = kits[i].setNewId(i + 1);
                data = ArrayUtils.addAll(data, tempKit.getMessage().getMessage());
            }
        }
        final SysexMessage result = new VdrumsSysexMessage();

        result.setMessage(data, data.length);
        return result;
    }

    /**
     * 
     * Gets a message in a Sysex format. Parses the bytes in the message into an
     * array of TdKit (TD-12) Each message of a kit must start with 0xF0 which
     * is (after masking) 240. The kit's address is 0x72 which is 114 and is
     * located in the eigth index (starting 0).
     * 
     * We ignore the status byte and adress byte of the "inner" parts of the
     * kits. We ignore by advancing the index (i) almost all the kit's size.
     * 
     * @param message
     *            SysexMessage
     * @return TdKit[]
     * @throws InvalidMidiDataException
     *             if generic problem in building the kits (like status byte)
     * @throws VdrumException
     *             can be bad checksum, not roland kit etc.
     */
    public TdKit[] sysexMessageToKits(SysexMessage message) throws InvalidMidiDataException,
            VdrumException {
        final byte[] byteMessage = message.getMessage();
        final Collection<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < byteMessage.length; i++) {
            if (((byteMessage[i] & 0xFF) == 240) && ((byteMessage[i + 7] & 0xFF) == 114)) {
                indexes.add(Integer.valueOf(i));
                i += VDrumsUtils.TD12_KIT_SIZE - 20;
            }
        }
        for (Integer index : indexes) {
            final int finishKitsMessage = index + VDrumsUtils.TD12_KIT_SIZE;
            final byte[] kitBytes = ArrayUtils.subarray(byteMessage, index, finishKitsMessage);
            final TdKit tempKit = FactoryKits.getKit(kitBytes);
            final int kitId = tempKit.getId();
            tdKits[kitId - 1] = tempKit;
        }
        return tdKits;
    }

}
