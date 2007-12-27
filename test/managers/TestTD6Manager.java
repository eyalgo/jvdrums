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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import kits.TdKit;
import kits.VdrumsSysexMessage;
import kits.td6.TD6Kit;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import resources.UtilsForTests;
import exceptions.VdrumException;

/**
 * @author egolan
 */
@Test(groups = { "manager6" }, dependsOnGroups = { "kits6" })
public final class TestTD6Manager {
    private byte[] getBytesFromFile(final String fileName) throws IOException,
            URISyntaxException {
        final File file = UtilsForTests.getFile(fileName);
        byte[] fileBytes = FileUtils.readFileToByteArray(file);
        return fileBytes;
    }

    private SysexMessage getMessageFromFile(final String fileName) throws URISyntaxException,
            IOException, InvalidMidiDataException {
        byte[] kitBytes = getBytesFromFile(fileName);
        SysexMessage message = new VdrumsSysexMessage();
        message.setMessage(kitBytes, kitBytes.length);
        return message;
    }

    public void checkChangeId97To32() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("td6Saz97.syx");
        TdKit kitFromMessage = new TD6Kit(message);
        TdKit[] kits = new TdKit[99];
        for (int i = 0; i < 99; i++) {
            kits[i] = null;
        }
        kits[31] = kitFromMessage;

        SysexMessage messageFromManager = TDManager.kitsToSysexMessage(kits);

        TdKit newKitFromManager = new TD6Kit(messageFromManager);
        Assert.assertTrue(newKitFromManager.getId() == 32);
        Assert.assertTrue(kitFromMessage.getId() == 97);
    }

    public void checkOneKitToSysexMessage() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("td6Saz97.syx");
        TdKit[] kitsFromManager = TDManager.sysexMessageToKits(message);
        TdKit maple3Kit = kitsFromManager[97];
        Assert.assertEquals(maple3Kit.getMessage(), message);
    }
}
