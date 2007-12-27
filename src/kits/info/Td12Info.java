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

/**
 * @author egolan
 */
public final class Td12Info extends TdInfo {
    public static final String NAME = "TD-12";
    public static final int SART_NAME_INDEX = 12;
    public static final int NAME_MAX_LENGTH = 13;
    public static final int NUMBER_OF_SUB_PARTS = 16;
    public final static int MAX_NUMBER_OF_KITS = 50;
    public static final int MSB_ADDRESS_INDEX = 7;
    public final static int KIT_SIZE = 2243;

    @Override
    public int getMaxLength() {
        return NAME_MAX_LENGTH;
    }

    @Override
    public int getNumberOfSubParts() {
        return NUMBER_OF_SUB_PARTS;
    }

    @Override
    public int getStartNameIndex() {
        return SART_NAME_INDEX;
    }

    @Override
    public int getMaxNumberOfKits() {
        return MAX_NUMBER_OF_KITS;
    }

    @Override
    public int getKitSize() {
        return KIT_SIZE;
    }

    @Override
    public int getMsbAddressIndex() {
        return MSB_ADDRESS_INDEX;
    }

    @Override
    String getNameToDisplay() {
        return NAME;
    }

}
