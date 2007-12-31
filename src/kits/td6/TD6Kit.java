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

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import kits.TdKit;
import kits.TdSubPart;
import kits.info.Td6Info;
import exceptions.BadMessageLengthException;
import exceptions.VdrumException;

/**
 * @author egolan
 */
public final class TD6Kit extends TdKit {
    private final int id;

    public TD6Kit(byte[] rawData) throws InvalidMidiDataException, VdrumException {
        // super(Td6Info.NUMBER_OF_SUB_PARTS, Td6Info.NAME_MAX_LENGTH, Td6Info.SART_NAME_INDEX,
        // Td6Info.MAX_NUMBER_OF_KITS);
        super(new Td6Info());
        if (rawData.length != tdInfo.getKitSize()) {
            throw new BadMessageLengthException(rawData.length);
        }
        subParts = new TdSubPart[tdInfo.getNumberOfSubParts()];
        for (int i = 0; i < subParts.length; i++) {
            boolean firstPart = (i == 0);
            subParts[i] = new TD6SubPart(rawData, i, firstPart, tdInfo.getMsbAddressIndex());
        }
        id = setImutableId();
    }

    private TD6Kit(TdSubPart[] subParts) {
        super(/*
                 * Td6Info.NUMBER_OF_SUB_PARTS, Td6Info.NAME_MAX_LENGTH,
                 * Td6Info.SART_NAME_INDEX, Td6Info.MAX_NUMBER_OF_KITS
                 */new Td6Info(), subParts);
        id = setImutableId();
    }

    public TD6Kit(SysexMessage sysexMessage) throws InvalidMidiDataException, VdrumException {
        this(sysexMessage.getMessage());
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    protected TdKit getNewKit(TdSubPart[] newSubParts) {
        TdKit newTd6Kit = new TD6Kit(newSubParts);
        return newTd6Kit;
    }

    @Override
    protected TdSubPart getNewSubPart(TdSubPart subPart, Integer newId)
            throws InvalidMidiDataException {
        return new TD6SubPart(subPart, newId, tdInfo.getMsbAddressIndex());
    }

    @Override
    public String getTdInfoName() {
        return tdInfo.getNameToDisplay();
    }
}
