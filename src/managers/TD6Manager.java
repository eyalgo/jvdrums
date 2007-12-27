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
import kits.info.Td6Info;
import kits.td6.TD6Kit;

import org.apache.commons.lang.ArrayUtils;

import exceptions.VdrumException;

/**
 * @author egolan
 */
final class TD6Manager implements TDModulesManager {

    @Override
    public TdKit[] sysexMessageToKits(SysexMessage message) throws InvalidMidiDataException,
            VdrumException {
        final byte[] byteMessage = message.getMessage();
        final Collection<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < byteMessage.length; i++) {
            if (((byteMessage[i] & 0xFF) == 240)
                    && ((byteMessage[i + Td6Info.MSB_ADDRESS_INDEX] & 0xFF) == 65)) {
                indexes.add(Integer.valueOf(i));
                i += Td6Info.KIT_SIZE - 20;
            }
        }
        final TdKit[] tdKits;
        tdKits = new TdKit[Td6Info.MAX_NUMBER_OF_KITS];
        for (int i = 0; i < tdKits.length; i++) {
            tdKits[i] = null;
        }
        for (Integer index : indexes) {
            final int finishKitsMessage = index + Td6Info.KIT_SIZE;
            final byte[] kitBytes = ArrayUtils.subarray(byteMessage, index, finishKitsMessage);
            final TdKit tempKit = getKit(kitBytes);
            final int kitId = tempKit.getId();
            tdKits[kitId - 1] = tempKit;
        }
        return tdKits;
    }

    @Override
    public TdKit getKit(byte[] kitBytes) throws InvalidMidiDataException, VdrumException {
        return new TD6Kit(kitBytes);
    }

}
