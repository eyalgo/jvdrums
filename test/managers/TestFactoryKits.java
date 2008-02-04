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

import kits.info.Td12Info;
import kits.info.Td6Info;
import kits.info.Td8Info;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import resources.UtilsForTests;
import exceptions.BadMessageLengthException;
import exceptions.UnsupportedModuleException;
import exceptions.VdrumException;

/**
 * @author egolan
 *
 */
@Test(groups = {"manager"})
public final class TestFactoryKits {
    private byte[] getFileBytes(final String fileName) throws URISyntaxException, IOException {
        final File file = UtilsForTests.getFile(fileName);
        byte[] fileBytes = FileUtils.readFileToByteArray(file);
        return fileBytes;
    }
    
    public void checkKitTd12() throws URISyntaxException, IOException, InvalidMidiDataException, VdrumException {
        byte[] kitBytes = getFileBytes("airtime20.syx");
        TDModulesManager kit = FactoryKits.getTdModuleManager(kitBytes);
        Assert.assertTrue(kit.getTdInfo() instanceof Td12Info);
    }
    
    @Test(expectedExceptions = UnsupportedModuleException.class)
    public void checkNotSupportedModule() throws URISyntaxException, IOException, InvalidMidiDataException, VdrumException {
        byte[] kitBytes = getFileBytes("notTd12.syx");
        FactoryKits.getTdModuleManager(kitBytes);
    }
    
    @Test(expectedExceptions = BadMessageLengthException.class)
    public void checkArrayOutOfBound() throws URISyntaxException, IOException, InvalidMidiDataException, VdrumException {
        byte[] kitBytes = getFileBytes("small.syx");
        FactoryKits.getTdModuleManager(kitBytes);
    }
    
    public void checkKitTd6() throws URISyntaxException, IOException, InvalidMidiDataException, VdrumException {
        byte[] kitBytes = getFileBytes("td6.syx");
        TDModulesManager kit = FactoryKits.getTdModuleManager(kitBytes);
        Assert.assertTrue(kit.getTdInfo() instanceof Td6Info);
    }
    
    public void checkKitTd8() throws URISyntaxException, IOException, InvalidMidiDataException, VdrumException {
        byte[] kitBytes = getFileBytes("td-8-64kits.syx");
        TDModulesManager kit = FactoryKits.getTdModuleManager(kitBytes);
        Assert.assertTrue(kit.getTdInfo() instanceof Td8Info);
    }
}
