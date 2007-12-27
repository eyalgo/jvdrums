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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import kits.TdKit;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import resources.UtilsForTests;
import exceptions.VdrumException;

/**
 * @author egolan
 */
@Test(groups = { "kits6" })
public final class TestKit6 {
    private TdKit kitSaz97;
    private SysexMessage kit1SysexMessage;
    
    @BeforeMethod
    public void setUp() throws IOException, InvalidMidiDataException, URISyntaxException,
            VdrumException {
        final File file1 = UtilsForTests.getFile("td6Saz97.syx");
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        kitSaz97 = new TD6Kit(kitBytes1);
        kit1SysexMessage = new SysexMessage();
        kit1SysexMessage.setMessage(kitBytes1, kitBytes1.length);
    }

    public void checkRawDataSyxMessage() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File file1 = UtilsForTests.getFile("td6Saz97.syx");
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        TdKit kitRaw = new TD6Kit(kitBytes1);

        SysexMessage message = new SysexMessage();
        message.setMessage(kitBytes1, kitBytes1.length);

        TdKit kitMessage = new TD6Kit(message);
        Assert.assertEquals(kitMessage, kitRaw);

        Assert.assertTrue(kitRaw.getId() == 97, "TD6Kit-Raw 97 with id " + kitRaw.getId());
        Assert.assertTrue(kitMessage.getId() == 97, "TD6Kit-Message 97 with id "
                + kitRaw.getId());
    }

    public void checkNameKit_1() throws URISyntaxException, IOException, InvalidMidiDataException, VdrumException {
        Assert.assertEquals("Saz", kitSaz97.getName(), "kitRaw should be Saz");
    }



    public void checkNewId() {
        try {
            TdKit kit32 = kitSaz97.setNewId(32);
            Assert.assertTrue(kit32.getId() == 32, "TD6Kit 32 with id 32");
            Assert.assertTrue(kitSaz97.getId() == 97,
                    "Check that the ID has not changed. TD6Kit 1 with id 1 (got "
                            + kitSaz97.getId() + ")");
        }
        catch (InvalidMidiDataException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void checkMessageOfKit1() {
        try {
            Assert.assertEquals(kit1SysexMessage.getMessage(), kitSaz97.getMessage().getMessage(),
                    "kitSazOrig message");
        }
        catch (InvalidMidiDataException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void checkBadId100() {
        try {
            kitSaz97.setNewId(100);
        }
        catch (InvalidMidiDataException e) {
            Assert.fail(e.getMessage());
        }
    }

//    public void checkEqualsAftterNewId() throws IOException, InvalidMidiDataException,
//            URISyntaxException, VdrumException {
//        File fileSaz = UtilsForTests.getFile("td6.syx");
//        byte[] saz97Bytes = FileUtils.readFileToByteArray(fileSaz);
//        TdKit kitSaz97 = new TD6Kit(saz97Bytes);
//        TdKit kitSaz12 = kitSaz97.setNewId(12);
//        File fileSaz12 = UtilsForTests.getFile("td6Saz12.syx");
//        byte[] saz12Bytes = FileUtils.readFileToByteArray(fileSaz12);
//        TdKit fileKitSaz12 = new TD6Kit(saz12Bytes);
//        Assert.assertEquals(kitSaz12, fileKitSaz12);
//    }

//    public void checkHashAftterNewId() throws IOException, InvalidMidiDataException,
//            URISyntaxException, VdrumException {
//        File fileSaz97 = UtilsForTests.getFile("td6.syx");
//        byte[] saz97Bytes = FileUtils.readFileToByteArray(fileSaz97);
//        TdKit kitSaz97 = new TD6Kit(saz97Bytes);
//        TdKit kitMaple4 = kitMaple3.setNewId(4);
//        File fileMaple4 = UtilsForTests.getFile("maple4.syx");
//        byte[] maple4Bytes = FileUtils.readFileToByteArray(fileMaple4);
//        TdKit fileKitMaple4 = new TD12Kit(maple4Bytes);
//        Assert.assertTrue(kitMaple4.hashCode() == fileKitMaple4.hashCode(),
//                "kitMaple4.hashCode=" + kitMaple4.hashCode() + " fileKitMaple4.hashCode="
//                        + fileKitMaple4.hashCode());
//    }
}
