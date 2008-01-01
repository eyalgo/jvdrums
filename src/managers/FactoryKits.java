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

package managers;

import kits.info.Td10ExpInfo;
import kits.info.Td12Info;
import kits.info.Td6Info;
import exceptions.BadMessageLengthException;
import exceptions.UnsupportedModuleException;

/**
 * @author egolan
 */
final class FactoryKits {
    private FactoryKits() {
    // No instance for this class
    }

    static TDModulesManager getTdModuleManager(final byte[] kitBytes)
            throws UnsupportedModuleException, BadMessageLengthException {
        try {
            if (((kitBytes[3] & 0xFF) == 0) && ((kitBytes[4] & 0xFF) == 0)
                    && ((kitBytes[5] & 0xFF) == 9)) {
                return new TDModulesManager(new Td12Info());
            }
            if (((kitBytes[3] & 0xFF) == 0) && ((kitBytes[4] & 0xFF) == 63)) {
                return new TDModulesManager(new Td6Info());
            }
//            if (((kitBytes[3] & 0xFF) == 0) && ((kitBytes[4] & 0xFF) == 10)) {
//                return new TDModulesManager(new Td10Info());
//            }
            if (((kitBytes[3] & 0xFF) == 0) && ((kitBytes[4] & 0xFF) == 45)) {
                return new TDModulesManager(new Td10ExpInfo());
            }
            throw new UnsupportedModuleException();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new BadMessageLengthException(kitBytes.length);
        }
    }
}
