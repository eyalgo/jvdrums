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

import midi.VdrumsSysexMessage;

import org.apache.commons.lang.ArrayUtils;

import utils.VDrumsUtils;
import exceptions.BadChecksumException;
import exceptions.NotRolandException;
import exceptions.VdrumException;

final class TD12SubPart extends VdrumsSysexMessage {
    private static final int ROLAND_ID_INDEX = 1;
    private static final int MSB_ADDRESS_INDEX = 7; // It is the 8th byte
    private static final int ID_ADDRESS_INDEX = 8;
    /** Number of bytes each sub part in the kit (raw data) */
    private static final int SIZE_SUB_PART = 141;
    /** The F part (16th) is smaller and has only 128 bytes */
    private static final int SIZE_LAST_SUB_PART = 128;
    private final int checksumIndex;

    TD12SubPart(byte[] kitRawData, final int location, boolean isLastPart)
            throws InvalidMidiDataException, VdrumException {
        int size;
        if (isLastPart) {
            size = SIZE_LAST_SUB_PART;
        } else {
            size = SIZE_SUB_PART;
        }
        final byte[] partRawData = ArrayUtils.subarray(kitRawData, location * SIZE_SUB_PART,
                location * SIZE_SUB_PART + size);
        checksumIndex = partRawData.length - 2;
        this.setMessage(partRawData, partRawData.length);
        int inputCheckSum = this.getMessage()[checksumIndex];
        final int checksum = VDrumsUtils.calculateChecksum(ArrayUtils.subarray(this
                .getMessage(), MSB_ADDRESS_INDEX, checksumIndex));
        if (inputCheckSum != checksum) {
            throw new BadChecksumException(inputCheckSum, checksum);
        }
        if (this.getMessage()[ROLAND_ID_INDEX] != VDrumsUtils.ROLAND_ID) {
            throw new NotRolandException(this.getMessage()[ROLAND_ID_INDEX]);
        }
    }

    /**
     * Copy Constructor. Gets the message from the original SysexMessage. The
     * getMessage() creates a copy of the data, so it won't be changed.
     * 
     * @param origtRawData
     * @throws InvalidMidiDataException
     */
    TD12SubPart(final TD12SubPart origtRawData, final int kitId)
            throws InvalidMidiDataException {
        final byte[] partRawData = origtRawData.getMessage();
        final Integer dataId = kitId - 1;
        partRawData[ID_ADDRESS_INDEX] = dataId.byteValue();
        checksumIndex = partRawData.length - 2;
        final int checksum = VDrumsUtils.calculateChecksum(ArrayUtils.subarray(partRawData,
                MSB_ADDRESS_INDEX, checksumIndex));
        partRawData[checksumIndex] = Integer.valueOf(checksum).byteValue();
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
