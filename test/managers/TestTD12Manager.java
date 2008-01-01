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
import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import kits.TdKit;
import kits.VdrumsSysexMessage;
import kits.info.Td12Info;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import resources.UtilsForTests;
import exceptions.BadChecksumException;
import exceptions.NotRolandException;
import exceptions.VdrumException;

@Test(groups = { "manager12" }, dependsOnGroups = { "kits12" })
public final class TestTD12Manager {

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

    @Test(dependsOnMethods = { "checkMessageFor50Kits", "checkMessageToKit25" })
    public void checkChangeId25To32() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("Bubbles25.syx");
        TdKit kitFromMessage = new TdKit(new Td12Info(),message);
        TdKit[] kits = new TdKit[50];
        for (int i = 0; i < 50; i++) {
            kits[i] = null;
        }
        kits[31] = kitFromMessage;

        SysexMessage messageFromManager = TDManager.kitsToSysexMessage(kits);

        TdKit newKitFromManager = new TdKit(new Td12Info(),messageFromManager);
        Assert.assertTrue(newKitFromManager.getId() == 32);
        Assert.assertTrue(kitFromMessage.getId() == 25);
    }

    @Test(dependsOnMethods = { "checkMessageFor50Kits", "checkMessageToKit25" })
    public void checkKitsToKits() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("myOrigKits.syx");
        TdKit[] kitsFromFile = TDManager.sysexMessageToKits(message);

        Object[] objects = ArrayUtils.addAll(null, kitsFromFile);
        TdKit[] changedKits = new TdKit[objects.length];
        for (int i = 0; i < objects.length; i++) {
            changedKits[i] = (TdKit) objects[i];
        }
        TdKit temp5 = changedKits[4];
        changedKits[4] = changedKits[9];
        changedKits[9] = temp5;

        Vector<TdKit> newFromManager = TDManager.kitsToKits(changedKits);
        Assert.assertTrue(newFromManager.size() == 50);
        int i = 0;
        for (TdKit kitFromManager : newFromManager) {
            if (kitFromManager == null) {
                Assert.fail(i + " is null");
            }
            i++;
        }
        Assert.assertEquals(kitsFromFile[4].getName(), newFromManager.elementAt(9).getName());
    }

    @Test(dependsOnMethods = { "checkMessageFor50Kits", "checkMessageToKit25" })
    public void checkOneKitToSysexMessage() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("maple3.syx");
        TdKit[] kitsFromManager = TDManager.sysexMessageToKits(message);
        TdKit maple3Kit = kitsFromManager[2];
        Assert.assertEquals(maple3Kit.getMessage(), message);
    }

    @Test(dependsOnMethods = { "checkMessageFor50Kits", "checkMessageToKit25" })
    public void checkOneKitBytesToSysexMessage() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final byte[] bytes = getBytesFromFile("maple3.syx");
        TdKit[] kitsFromManager = TDManager.bytesToKits(bytes);
        TdKit maple3Kit = kitsFromManager[2];
        Assert.assertEquals(maple3Kit.getMessage().getMessage(), bytes);
    }

    @Test(dependsOnMethods = { "checkMessageFor50Kits" })
    public void check50KitsToSysexMessage() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("myOrigKits.syx");
        TdKit[] kitsFromManager = TDManager.sysexMessageToKits(message);// dependsOnMethods
        byte[] data = null;
        for (int i = 0; i < kitsFromManager.length; i++) {
            data = ArrayUtils.addAll(data, kitsFromManager[i].getMessage().getMessage());
        }
        final SysexMessage result = new VdrumsSysexMessage();

        result.setMessage(data, data.length);
        SysexMessage messageFromManager = TDManager.kitsToSysexMessage(kitsFromManager);
        Assert.assertEquals(result, messageFromManager);
    }

    public void checkMessageToKit25() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("Bubbles25.syx");

        TdKit[] kitsFromManager = TDManager.sysexMessageToKits(message);
        for (int i = 0; i < kitsFromManager.length; i++) {
            if (i != 24) {
                if (kitsFromManager[i] != null) {
                    Assert.fail(i + " is not null");
                }
            }
        }
        final File file25 = UtilsForTests.getFile("Bubbles25.syx");
        byte[] kitBytes25 = FileUtils.readFileToByteArray(file25);
        TdKit kit25 = new TdKit(new Td12Info(),kitBytes25);
        Assert.assertEquals(kitsFromManager[24], kit25);
    }

    public void checkBytesToKit25() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final byte[] bytes = getBytesFromFile("Bubbles25.syx");

        TdKit[] kitsFromManager = TDManager.bytesToKits(bytes);
        for (int i = 0; i < kitsFromManager.length; i++) {
            if (i != 24) {
                if (kitsFromManager[i] != null) {
                    Assert.fail(i + " is not null");
                }
            }
        }
        final File file25 = UtilsForTests.getFile("Bubbles25.syx");
        byte[] kitBytes25 = FileUtils.readFileToByteArray(file25);
        TdKit kit25 = new TdKit(new Td12Info(),kitBytes25);
        Assert.assertEquals(kitsFromManager[24], kit25);
    }

    public void checkMessageFor50Kits() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("myOrigKits.syx");
        TdKit[] kitsFromManager = TDManager.sysexMessageToKits(message);
        if (kitsFromManager.length != 50) {
            Assert.fail("Got " + kitsFromManager.length + " instead of 50");
        }
        for (int i = 0; i < kitsFromManager.length; i++) {
            if (kitsFromManager[i].getId() != i + 1) {
                Assert.fail("kitsFromManager[i].getId() " + kitsFromManager[i].getId() + " i="
                        + i);
            }
        }
    }

    public void checkBytesFor50Kits() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final byte[] bytes = getBytesFromFile("myOrigKits.syx");
        TdKit[] kitsFromManager = TDManager.bytesToKits(bytes);
        if (kitsFromManager.length != 50) {
            Assert.fail("Got " + kitsFromManager.length + " instead of 50");
        }
        for (int i = 0; i < kitsFromManager.length; i++) {
            if (kitsFromManager[i].getId() != i + 1) {
                Assert.fail("kitsFromManager[i].getId() " + kitsFromManager[i].getId() + " i="
                        + i);
            }
        }
    }

    @Test(expectedExceptions = InvalidMidiDataException.class)
    public void checkMessageBadStat() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        TDManager.sysexMessageToKits(getMessageFromFile("kitsBadStat.syx"));
    }

    @Test(expectedExceptions = InvalidMidiDataException.class)
    public void checkBytesBadStat() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        TDManager.bytesToKits(getBytesFromFile("kitsBadStat.syx"));
    }

    @Test(expectedExceptions = NotRolandException.class)
    public void checkMessageNotRoland() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        TDManager.sysexMessageToKits(getMessageFromFile("maple3NotRoland.syx"));
    }

    @Test(expectedExceptions = NotRolandException.class)
    public void checkBytesNotRoland() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        TDManager.bytesToKits(getBytesFromFile("maple3NotRoland.syx"));
    }

    @Test(expectedExceptions = BadChecksumException.class , dependsOnGroups = { "exception12" })
    public void checkMessageBadChecksum() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        TDManager.sysexMessageToKits(getMessageFromFile("maple3BadChecksum.syx"));
    }

    @Test(expectedExceptions = BadChecksumException.class, dependsOnGroups = { "exception12" })
    public void checkBytesBadChecksum() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        TDManager.bytesToKits(getBytesFromFile("maple3BadChecksum.syx"));
    }
}
