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

import kits.TdSubPart;
import exceptions.VdrumException;

public final class TD12SubPart extends TdSubPart {
    private static final int ID_ADDRESS_INDEX = 8;
    /** Number of bytes each sub part in the kit (raw data) */
    private static final int SIZE_SUB_PART = 141;
    /** The F part (16th) is smaller and has only 128 bytes */
    private static final int SIZE_LAST_SUB_PART = 128;

    TD12SubPart(byte[] kitRawData, final int location, boolean isLastPart, int msbAddressIndex)
            throws InvalidMidiDataException, VdrumException {
        super(ID_ADDRESS_INDEX, msbAddressIndex);
        int size;
        if (isLastPart) {
            size = SIZE_LAST_SUB_PART;
        } else {
            size = SIZE_SUB_PART;
        }
        int from = location * SIZE_SUB_PART;
        int to = location * SIZE_SUB_PART + size;
        createData(kitRawData, from, to);
    }

    TD12SubPart(final TdSubPart origtRawData, final int kitId, int msbAddressIndex)
            throws InvalidMidiDataException {
        super(ID_ADDRESS_INDEX, msbAddressIndex);
        copyConstructor(origtRawData, kitId);
    }
}
