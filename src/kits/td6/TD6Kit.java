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
import kits.VdrumsSysexMessage;

import org.apache.commons.lang.ArrayUtils;

import utils.VDrumsUtils;
import exceptions.BadMessageLengthException;
import exceptions.VdrumException;

/**
 * @author egolan
 */
public final class TD6Kit implements TdKit {
    private static final int TD_6_NUMBER_OF_SUB_PARTS = 13;
    private final TdSubPart[] subParts;
    private final int id;

    public TD6Kit(byte[] rawData) throws InvalidMidiDataException, VdrumException {
        if (rawData.length != VDrumsUtils.TD6_KIT_SIZE) {
            throw new BadMessageLengthException(rawData.length);
        }
        subParts = new TdSubPart[TD_6_NUMBER_OF_SUB_PARTS];
        for (int i = 0; i < subParts.length; i++) {
            boolean firstPart = (i == 0);
            subParts[i] = new TD6SubPart(rawData, i, firstPart);
        }
        id = setImutableId();
    }

    public TD6Kit(SysexMessage sysexMessage) throws InvalidMidiDataException, VdrumException {
        this(sysexMessage.getMessage());
    }

    private int setImutableId() {
        int result = 0;
        for (int i = 0; i < subParts.length; i++) {
            result += this.subParts[i].getId();
        }
        if (result % subParts.length != 0) {
            throw new RuntimeException();
        }
        result = result / subParts.length;

        return result;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public VdrumsSysexMessage[] getKitSubParts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SysexMessage getMessage() throws InvalidMidiDataException {
        byte[] data = null;
        for (int i = 0; i < TD_6_NUMBER_OF_SUB_PARTS; i++) {
            data = ArrayUtils.addAll(data, subParts[i].getMessage());
        }
        final SysexMessage result = new VdrumsSysexMessage();

        result.setMessage(data, data.length);
        return result;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TdKit setNewId(Integer newId) throws InvalidMidiDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        result.append(newLine);
        result.append(this.getClass().getSimpleName()).append(" {");
        result.append(newLine);
        result.append(" Name: ").append(this.getName()).append(newLine);
        result.append(" id: ").append(this.getId()).append(newLine);
        result.append("}");
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof TD6Kit) {
            final TD6Kit other = (TD6Kit) obj;
            try {
                return this.getMessage().equals(other.getMessage());
            }
            catch (InvalidMidiDataException e) {
                throw new RuntimeException("Got InvalidMidiDataException in equals "
                        + this.toString());
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        for (int i = 0; i < subParts.length; i++) {
            result = 23 * result + subParts[i].hashCode();
        }
        return result;
    }
}
