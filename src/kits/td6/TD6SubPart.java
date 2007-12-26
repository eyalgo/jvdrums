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

import midi.VdrumsSysexMessage;

import org.apache.commons.lang.ArrayUtils;

import exceptions.VdrumException;

/**
 * @author egolan
 */
final class TD6SubPart extends VdrumsSysexMessage {
    private static final int SIZE_SUB_PART = 55;
    private static final int SIZE_FIRST_SUB_PART = 37;
    private static final int ID_ADDRESS_INDEX = 7;

    public TD6SubPart(final byte[] kitRawData, final int location, final boolean firstPart)
            throws InvalidMidiDataException, VdrumException {
        int size;
        if (firstPart) {
            size = SIZE_FIRST_SUB_PART;
        } else {
            size = SIZE_SUB_PART;
        }
        int firstIndex;
        if (firstPart) {
            firstIndex = 0;
        } else {
            firstIndex = SIZE_FIRST_SUB_PART + (location-1) * SIZE_SUB_PART;
        }

        final byte[] partRawData = ArrayUtils.subarray(kitRawData, firstIndex, firstIndex
                + size);
        this.setMessage(partRawData, partRawData.length);
    }

    int getId() {
        final int msbAddress = getMessage()[ID_ADDRESS_INDEX];
        return msbAddress + 1;
    }

    @Override
    public int hashCode() {
        final byte[] partRawData = this.getMessage();
        int result = 23;
        for (int i = 0; i < partRawData.length; i++) {
            result = 17 * result + (partRawData[i] & 0xFF);
        }
        return result;
    }
}
