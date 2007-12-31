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
import exceptions.VdrumException;

/**
 * @author egolan
 */
public abstract class TdInfo {
    private final String name;
    private final int startNameIndex;
    private final int maxNameLength;
    private final int numberOfSubParts;
    private final int maxNumberOfKits;
    private final int msbAddressIndex;
    private final int kitSize;
    private final int kitIdIndex;
    private final int subPartIndex;
    private final int msbAddressValue;

    TdInfo(String name, int startNameIndex, int maxNameLength, int numberOfSubParts,
            int maxNumberOfKits, int msbAddressIndex, int msbAddressValue, int kitSize,
            int kitIdIndex, int subPartIndex) {
        this.name = name;
        this.startNameIndex = startNameIndex;
        this.maxNameLength = maxNameLength;
        this.numberOfSubParts = numberOfSubParts;
        this.maxNumberOfKits = maxNumberOfKits;
        this.msbAddressIndex = msbAddressIndex;
        this.msbAddressValue = msbAddressValue;
        this.kitSize = kitSize;
        this.kitIdIndex = kitIdIndex;
        this.subPartIndex = subPartIndex;
    }

    public final int getStartNameIndex() {
        return this.startNameIndex;
    }

    public final int getMaxLengthName() {
        return this.maxNameLength;
    }

    public final int getNumberOfSubParts() {
        return numberOfSubParts;
    }

    public final int getMaxNumberOfKits() {
        return maxNumberOfKits;
    }

    public final int getKitSize() {
        return kitSize;
    }

    public final int getMsbAddressIndex() {
        return msbAddressIndex;
    }

    public final String getNameToDisplay() {
        return name;
    }

    public final int getKitIdIndex() {
        return kitIdIndex;
    }

    public final int getSubPartIndex() {
        return subPartIndex;
    }

    public final int getMsbAddressValue() {
        return msbAddressValue;
    }

    @Override
    public final boolean equals(Object obj) {
        return getNameToDisplay().equals(((TdInfo) obj).getNameToDisplay());
    }

    @Override
    public final int hashCode() {
        return getNameToDisplay().hashCode();
    }

    @Override
    public final String toString() {
        return getNameToDisplay();
    }

    public abstract TdKit getKit(byte[] kitBytes) throws InvalidMidiDataException,
            VdrumException;

    public abstract TdKit getNewKit(TdSubPart[] newSubParts);

    public final TdSubPart getNewSubPart(TdSubPart subPart, Integer newId)
            throws InvalidMidiDataException {
        return new TdSubPart(subPart, newId, this);
    }
}
