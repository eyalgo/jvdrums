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

package kits.td10;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import kits.TdKit;
import kits.info.Td10ExpInfo;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import resources.UtilsForTests;
import exceptions.BadChecksumException;
import exceptions.BadMessageLengthException;
import exceptions.NotRolandException;
import exceptions.VdrumException;

/**
 * @author egolan
 */
@Test(groups = { "kits10" })
public final class TestKit10 {
    // private TdKit kit50;
    // private SysexMessage kit50SysexMessage;

    public void checkRawDataSyxMessage() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File file1 = UtilsForTests.getFile("td10_7_tr808.syx");
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        TdKit kitRaw = new TdKit(new Td10ExpInfo(), kitBytes1);

        SysexMessage message = new SysexMessage();
        message.setMessage(kitBytes1, kitBytes1.length);

        TdKit kitMessage = new TdKit(new Td10ExpInfo(), message);
        Assert.assertEquals(kitMessage, kitRaw);

        Assert.assertTrue(kitRaw.getId() == 7, "TD10Kit-Raw 7 with id " + kitRaw.getId());
        Assert.assertTrue(kitMessage.getId() == 7, "TD10Kit-Message 7 with id "
                + kitRaw.getId());
    }

    public void checkNameKit_1() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File file1 = UtilsForTests.getFile("td10_7_tr808.syx");
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        TdKit kit = new TdKit(new Td10ExpInfo(), kitBytes1);
        Assert.assertEquals("Mr.TR808", kit.getName(), "kitRaw should be Mr.TR808");
    }

    public void checkNewId() throws URISyntaxException, IOException,
            BadMessageLengthException, BadChecksumException, NotRolandException {
        try {
            final File file1 = UtilsForTests.getFile("td10_7_tr808.syx");
            byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
            TdKit kit = new TdKit(new Td10ExpInfo(), kitBytes1);
            TdKit kit32 = kit.setNewId(32);
            Assert.assertTrue(kit32.getId() == 32, "TD10Kit 32 with id 32");
            Assert.assertTrue(kit.getId() == 7,
                    "Check that the ID has not changed. TD10Kit 7 with id 7 (got "
                            + kit.getId() + ")");
        }
        catch (InvalidMidiDataException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void checkBadId100() throws URISyntaxException, IOException,
            BadMessageLengthException, BadChecksumException, NotRolandException,
            InvalidMidiDataException {
        final File file1 = UtilsForTests.getFile("td10_7_tr808.syx");
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        TdKit kit = new TdKit(new Td10ExpInfo(), kitBytes1);
        try {
            kit.setNewId(51);
        }
        catch (InvalidMidiDataException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void checkEqualsandHashAftterNewId() throws IOException, InvalidMidiDataException,
            URISyntaxException, VdrumException {
        final File file1 = UtilsForTests.getFile("td10_7_tr808.syx");
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        TdKit kit = new TdKit(new Td10ExpInfo(), kitBytes1);
        TdKit kitSaz6 = kit.setNewId(6);
        File fileSaz6 = UtilsForTests.getFile("td10_6_tr808.syx");
        byte[] saz6Bytes = FileUtils.readFileToByteArray(fileSaz6);
        TdKit fileKitSaz6 = new TdKit(new Td10ExpInfo(), saz6Bytes);
        Assert.assertEquals(kitSaz6, fileKitSaz6);
        Assert.assertTrue(kitSaz6.hashCode() == fileKitSaz6.hashCode(), "td10_7_tr808.hashCode="
                + kitSaz6.hashCode() + " td10_7_tr808.hashCode=" + fileKitSaz6.hashCode());
    }
}
