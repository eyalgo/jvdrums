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
import kits.td12.TD12Kit;
import managers.TDManager;
import midi.VdrumsSysexMessage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import resources.Utils;
import exceptions.BadChecksumException;
import exceptions.NotRolandException;
import exceptions.VdrumException;

@Test(groups = {"manager"}, dependsOnGroups = {"kits", "exception"})
public final class TestTD12Manager {
    private TDManager td12Manager;

    @BeforeMethod
    public void setUp() throws IOException, InvalidMidiDataException, URISyntaxException,
            VdrumException {
        td12Manager = new TDManager();
    }

    private SysexMessage getMessageFromFile(final String fileName) throws URISyntaxException,
            IOException, InvalidMidiDataException {
        final File file = Utils.getTestFile(fileName);
        byte[] kitBytes = FileUtils.readFileToByteArray(file);
        SysexMessage message = new VdrumsSysexMessage();
        message.setMessage(kitBytes, kitBytes.length);
        return message;
    }
    
    @Test(dependsOnMethods = {"checkMessageFor50Kits", "checkMessageToKit25" })
    public void checkChangeId25To32() throws URISyntaxException, IOException, InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("Bubbles25.syx");
        TdKit kitFromMessage = new TD12Kit(message);
        TdKit[] kits = new TdKit[50];
        for (int i = 0; i<50;i++) {
            kits[i] = null;
        }
        kits[31] = kitFromMessage;
        
        SysexMessage messageFromManager = td12Manager.kitsToSysexMessage(kits);
    
        TdKit newKitFromManager = new TD12Kit(messageFromManager);
        Assert.assertTrue(newKitFromManager.getId() == 32);
        Assert.assertTrue(kitFromMessage.getId() == 25);
    }
    
    @Test(dependsOnMethods = { "checkMessageFor50Kits", "checkMessageToKit25" })
    public void checkKitsToKits() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("myOrigKits.syx");
        TdKit[] kitsFromManager = td12Manager.sysexMessageToKits(message);
        
        Object[] objects = ArrayUtils.addAll(null, kitsFromManager);
        TdKit[] changedKits = new TdKit[objects.length];
        for (int i=0;i<objects.length;i++) {
            changedKits[i] = (TdKit)objects[i];
        }
        TdKit temp5 = changedKits[4];
        changedKits[4] = changedKits[9];
        changedKits[9] = temp5;
        
        TdKit[] newFromManager = td12Manager.kitsToKits(changedKits);
        Assert.assertTrue(newFromManager.length == 50);
        for (int i=0;i<newFromManager.length;i++) {
            if (newFromManager[i] == null) {
                Assert.fail(i + " is null");
            }
        }
        Assert.assertEquals(kitsFromManager[4].getName(), changedKits[9].getName());
    }
    
    @Test(dependsOnMethods = { "checkMessageFor50Kits", "checkMessageToKit25" })
    public void checkOneKitToSysexMessage() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("maple3.syx");
        TdKit[] kitsFromManager = td12Manager.sysexMessageToKits(message);// dependsOnMethods
        TdKit maple3Kit = kitsFromManager[2];
        Assert.assertEquals(maple3Kit.getMessage(), message);
    }

    @Test(dependsOnMethods = { "checkMessageFor50Kits" })
    public void check50KitsToSysexMessage() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("myOrigKits.syx");
        TdKit[] kitsFromManager = td12Manager.sysexMessageToKits(message);// dependsOnMethods
        byte[] data = null;
        for (int i = 0; i < kitsFromManager.length; i++) {
            data = ArrayUtils.addAll(data, kitsFromManager[i].getMessage().getMessage());
        }
        final SysexMessage result = new VdrumsSysexMessage();

        result.setMessage(data, data.length);
        SysexMessage messageFromManager = td12Manager.kitsToSysexMessage(kitsFromManager);
        Assert.assertEquals(result, messageFromManager);
    }

    public void checkMessageToKit25() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("Bubbles25.syx");

        TdKit[] kitsFromManager = td12Manager.sysexMessageToKits(message);
        for (int i = 0; i < kitsFromManager.length; i++) {
            if (i != 24) {
                if (kitsFromManager[i] != null) {
                    Assert.fail(i + " is not null");
                }
            }
        }
        final File file25 = Utils.getTestFile("Bubbles25.syx");
        byte[] kitBytes25 = FileUtils.readFileToByteArray(file25);
        TdKit kit25 = new TD12Kit(kitBytes25);
        Assert.assertEquals(kitsFromManager[24], kit25);
    }

    public void checkMessageFor50Kits() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final SysexMessage message = getMessageFromFile("myOrigKits.syx");
        TdKit[] kitsFromManager = td12Manager.sysexMessageToKits(message);
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
        td12Manager.sysexMessageToKits(getMessageFromFile("kitsBadStat.syx"));
    }

    @Test(expectedExceptions = NotRolandException.class)
    public void checkMessageNotRoland() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        td12Manager.sysexMessageToKits(getMessageFromFile("maple3NotRoland.syx"));
    }

    @Test(expectedExceptions = BadChecksumException.class)
    public void checkMessageBadChecksum() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        td12Manager.sysexMessageToKits(getMessageFromFile("maple3BadChecksum.syx"));
    }
}
