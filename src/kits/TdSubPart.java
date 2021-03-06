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

package kits;

import java.io.Serializable;

import javax.sound.midi.InvalidMidiDataException;

import jvdrums.JVDrumsLogger;
import kits.info.TdInfo;

import org.apache.commons.lang.ArrayUtils;

import exceptions.NotRolandException;
import exceptions.VdrumException;

/**
 * @author egolan
 */
public final class TdSubPart extends VdrumsSysexMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static int ROLAND_ID = 65; // 0x41
    private static final int ROLAND_ID_INDEX = 1;
    private final TdInfo tdInfo;

    private TdSubPart(TdInfo tdInfo) {
        this.tdInfo = tdInfo;
    }

    public TdSubPart(byte[] partRawData, TdInfo tdInfo) throws InvalidMidiDataException,
            VdrumException {
        this(tdInfo);
        createData(partRawData);
    }

    public TdSubPart(final TdSubPart origtRawData, final int kitId, TdInfo tdInfo)
            throws InvalidMidiDataException {
        this(tdInfo);
        copyConstructor(origtRawData, kitId);
    }

    final int getId() {
        final int msbAddress = getMessage()[tdInfo.getKitIdIndex()];
        return msbAddress + 1;
    }

    @Override
    public final int hashCode() {
        final byte[] partRawData = this.getMessage();
        int result = 23;
        for (int i = 0; i < partRawData.length; i++) {
            result = 17 * result + (partRawData[i] & 0xFF);
        }
        return result;
    }

    /**
     * Copy Constructor. Gets the message from the original SysexMessage. The getMessage()
     * creates a copy of the data, so it won't be changed.
     * 
     * @param origtRawData
     * @throws InvalidMidiDataException
     */
    private final void copyConstructor(final TdSubPart origtRawData, final int kitId)
            throws InvalidMidiDataException {
        final byte[] partRawData = origtRawData.getMessage();
        final Integer dataId = kitId - 1;
        partRawData[tdInfo.getKitIdIndex()] = dataId.byteValue();
        int checksumIndex = partRawData.length - 2;
        final int checksum = calculateChecksum(ArrayUtils.subarray(partRawData, tdInfo
                .getMsbAddressIndex(), checksumIndex));
        partRawData[checksumIndex] = Integer.valueOf(checksum).byteValue();
        this.setMessage(partRawData, partRawData.length);
    }

    private final void createData(byte[] partRawData) throws InvalidMidiDataException,
            VdrumException {
        int checksumIndex = partRawData.length - 2;
        this.setMessage(partRawData, partRawData.length);
        int inputCheckSum = this.getMessage()[checksumIndex];
        final int checksum = calculateChecksum(ArrayUtils.subarray(this.getMessage(), tdInfo
                .getMsbAddressIndex(), checksumIndex));
        if (inputCheckSum != checksum) {
            JVDrumsLogger.getLogger().error(
                    "Checksum problem calculated: " + checksum + " message checksum: "
                            + inputCheckSum);
            // throw new BadChecksumException(inputCheckSum, checksum);
        }
        if (this.getMessage()[ROLAND_ID_INDEX] != ROLAND_ID) {
            throw new NotRolandException(this.getMessage()[ROLAND_ID_INDEX]);
        }
    }

    // protected final void createData(byte[] kitRawData, int from, int to)
    // throws InvalidMidiDataException, BadChecksumException, NotRolandException {
    // final byte[] partRawData = ArrayUtils.subarray(kitRawData, from, to);
    // this.createData(partRawData);
    // }

    private int calculateChecksum(final byte[] data) {
        int sum = 0;
        for (int i = 0; i < data.length; i++) {
            int currentInt = data[i] & 0xFF;
            sum += currentInt;
        }
        final int reminder = sum % 128;
        if (reminder == 0) {
            return 0;
        }
        final int checksum = 128 - reminder;
        return checksum;
    }
}
