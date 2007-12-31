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

package kits.td6;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import kits.TdKit;
import kits.TdSubPart;
import kits.info.Td6Info;

import org.apache.commons.lang.ArrayUtils;

import exceptions.BadMessageLengthException;
import exceptions.VdrumException;

/**
 * @author egolan
 */
public final class TD6Kit extends TdKit {
    private final int id;

    public TD6Kit(SysexMessage sysexMessage) throws InvalidMidiDataException, VdrumException {
        this(sysexMessage.getMessage());
    }

    public TD6Kit(byte[] rawData) throws InvalidMidiDataException, VdrumException {
        super(new Td6Info(), rawData);
//        if (rawData.length != tdInfo.getKitSize()) {
//            throw new BadMessageLengthException(rawData.length);
//        }
//        subParts = new TdSubPart[tdInfo.getNumberOfSubParts()];
//        
//        final List<Integer> indexes = new ArrayList<Integer>();
//        for (int i = 0; i < rawData.length; i++) {
//            if ((rawData[i] & 0xFF) == 240) {
//                indexes.add(i);
//            }
//        }
//        try {
//            for (int i = 0; i < subParts.length; i++) {
//                int from = indexes.get(i);
//                int to;
//                if (i == (subParts.length - 1)) {
//                    to = rawData.length;
//                } else {
//                   to =  indexes.get(i+1);
//                }
//                final byte[] partRawData = ArrayUtils.subarray(rawData, from, to);
//                subParts[i] = new TdSubPart(partRawData, tdInfo);
//            }
//            
//        }
//        catch (RuntimeException e) {
//            throw new InvalidMidiDataException();
//        }
        id = setImutableId();
    }

    public TD6Kit(TdSubPart[] subParts) {
        super(new Td6Info(), subParts);
        id = setImutableId();
    }

    @Override
    public int getId() {
        return this.id;
    }
}
