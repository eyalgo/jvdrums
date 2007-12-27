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

package kits.td12;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import kits.TdKit;
import kits.TdSubPart;
import kits.info.Td12Info;
import exceptions.BadMessageLengthException;
import exceptions.VdrumException;

public final class TD12Kit extends TdKit {
    private final int id;

    public TD12Kit(SysexMessage sysexMessage) throws InvalidMidiDataException, VdrumException {
        this(sysexMessage.getMessage());
    }

    public TD12Kit(byte[] rawData) throws InvalidMidiDataException, VdrumException {
        super(Td12Info.NUMBER_OF_SUB_PARTS, Td12Info.NAME_MAX_LENGTH, Td12Info.SART_NAME_INDEX,
                Td12Info.MAX_NUMBER_OF_KITS);
        if (rawData.length != Td12Info.KIT_SIZE) {
            throw new BadMessageLengthException(rawData.length);
        }
        subParts = new TdSubPart[Td12Info.NUMBER_OF_SUB_PARTS];
        for (int i = 0; i < subParts.length; i++) {
            boolean lastPart = (i == (Td12Info.NUMBER_OF_SUB_PARTS - 1));
            subParts[i] = new TD12SubPart(rawData, i, lastPart);
        }
        id = setImutableId();
    }

    private TD12Kit(TdSubPart[] subParts) {
        super(Td12Info.NUMBER_OF_SUB_PARTS, Td12Info.NAME_MAX_LENGTH, Td12Info.SART_NAME_INDEX,
                Td12Info.MAX_NUMBER_OF_KITS, subParts);
        id = setImutableId();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    protected TdSubPart getNewSubPart(TdSubPart subPart, Integer newId)
            throws InvalidMidiDataException {
        return new TD12SubPart(subPart, newId);
    }

    @Override
    protected TdKit getNewKit(TdSubPart[] newSubParts) {
        TdKit newTd12Kit = new TD12Kit(newSubParts);
        return newTd12Kit;
    }
}
