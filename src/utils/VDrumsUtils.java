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

package utils;

public final class VDrumsUtils {
    public final static int MAX_NUMBER_OF_KITS = 50;
    public final static int ROLAND_ID = 65; // 0x41
    /**
     * Number of bytes for each kit.
     * It is 0x8C3 in HEX representation
     */
    public final static int TD12_KIT_SIZE = 2243;
    
    /**
     * Calculates sum according to TD-12 MIDI Implementation (p.12)
     * sum of bytes
     * sum mod 128 = reminder
     * 128 - reminder = checksum
     * 
     * @param data byte[]
     * @return
     */
    public static int calculateChecksum(final byte[] data){
        int sum = 0;
        for (int i=0;i<data.length;i++){
            int currentInt = (int)data[i]& 0xFF;
            sum += currentInt;
        }
        final int reminder = sum % 128;
        if (reminder == 0) {
            return 0;
        }
        final int checksum = 128 - reminder;
        return checksum;
    }

    private VDrumsUtils() {
    // The class should not be have instances
    }
}
