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
public final class Td10ExpInfo extends TdInfo {
    private static final long serialVersionUID = -907843400291653140L;
    private static final String NAME = "TD-10 (& EXP)";
    private static final int START_NAME_INDEX = 10;
    private static final int NAME_MAX_LENGTH = 8;
    private static final int NUMBER_OF_SUB_PARTS = 13;
    private final static int MAX_NUMBER_OF_KITS = 50;
    private static final int MSB_ADDRESS_INDEX = 6;
    private static final int MSB_ADDRESS_VALUE = 65;
    private final static int KIT_SIZE = 906;

    public Td10ExpInfo() {
        super(NAME, START_NAME_INDEX, NAME_MAX_LENGTH, NUMBER_OF_SUB_PARTS,
                MAX_NUMBER_OF_KITS, MSB_ADDRESS_INDEX, MSB_ADDRESS_VALUE, KIT_SIZE,
                MSB_ADDRESS_INDEX + 1, MSB_ADDRESS_INDEX + 2);
    }
}
