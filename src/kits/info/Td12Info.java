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

package kits.info;

import javax.sound.midi.InvalidMidiDataException;

import kits.TdKit;
import kits.TdSubPart;
import kits.td12.TD12Kit;
import exceptions.VdrumException;

/**
 * @author egolan
 */
public final class Td12Info extends TdInfo {
    private static final String NAME = "TD-12";
    private static final int START_NAME_INDEX = 12;
    private static final int NAME_MAX_LENGTH = 13;
    private static final int NUMBER_OF_SUB_PARTS = 16;
    private static final int MAX_NUMBER_OF_KITS = 50;
    private static final int MSB_ADDRESS_INDEX = 7;
    private static final int MSB_ADDRESS_VALUE = 114;
    private static final int KIT_SIZE = 2243;

    public Td12Info() {
        super(NAME, START_NAME_INDEX, NAME_MAX_LENGTH, NUMBER_OF_SUB_PARTS,
                MAX_NUMBER_OF_KITS, MSB_ADDRESS_INDEX, MSB_ADDRESS_VALUE, KIT_SIZE,
                MSB_ADDRESS_INDEX + 1, MSB_ADDRESS_INDEX + 2);
    }

    @Override
    public TdKit getKit(byte[] kitBytes) throws InvalidMidiDataException, VdrumException {
        return new TD12Kit(kitBytes);
    }

    @Override
    public TdKit getNewKit(TdSubPart[] newSubParts) {
        return new TD12Kit(newSubParts);
    }
}
