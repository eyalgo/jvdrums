package kits.td8;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import kits.TdKit;
import kits.info.Td8Info;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import resources.UtilsForTests;
import exceptions.VdrumException;

@Test(groups = { "kits8" })
public final class TestKit8 {
    private final static String TD8FILE = "td-8-64kits.syx";
    public void checkRawDataSyxMessage() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File file1 = UtilsForTests.getFile("Sizzle_2.syx");
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        TdKit kitRaw = new TdKit(new Td8Info(), kitBytes1);

        SysexMessage message = new SysexMessage();
        message.setMessage(kitBytes1, kitBytes1.length);

        TdKit kitMessage = new TdKit(new Td8Info(), message);
        Assert.assertEquals(kitMessage, kitRaw);

        Assert.assertTrue(kitRaw.getId() == 2, "TD8Kit-Raw 2 with id " + kitRaw.getId());
        Assert.assertTrue(kitMessage.getId() == 2, "TD8Kit-Message 2 with id "
                + kitRaw.getId());
    }

    public void checkNameKit_1() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File file1 = UtilsForTests.getFile("Sizzle_2.syx");
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        TdKit kit = new TdKit(new Td8Info(), kitBytes1);
        Assert.assertEquals("Sizzle", kit.getName(), "kitRaw should be Sizzle");
    }

    public void checkNewId() throws URISyntaxException, IOException, VdrumException {
        try {
            final File file1 = UtilsForTests.getFile("td10-homeboy-4.syx");
            byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
            TdKit kit = new TdKit(new Td8Info(), kitBytes1);
            TdKit kit32 = kit.setNewId(32);
            Assert.assertTrue(kit32.getId() == 32, "TD8Kit 32 with id 32");
            Assert.assertTrue(kit.getId() == 4,
                    "Check that the ID has not changed. TD8Kit 4 with id 4 (got "
                            + kit.getId() + ")");
        }
        catch (InvalidMidiDataException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void checkBadId67() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File file1 = UtilsForTests.getFile(TD8FILE);
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        TdKit kit = new TdKit(new Td8Info(), kitBytes1);
        try {
            kit.setNewId(67);
        }
        catch (InvalidMidiDataException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void checkEqualsandHashAftterNewId() throws IOException, InvalidMidiDataException,
            URISyntaxException, VdrumException {
        final File file1 = UtilsForTests.getFile("~Custom1.syx");
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        TdKit kit = new TdKit(new Td8Info(), kitBytes1);
        TdKit kitSaz6 = kit.setNewId(6);
        File fileSaz6 = UtilsForTests.getFile("~Custom6.syx");
        byte[] saz6Bytes = FileUtils.readFileToByteArray(fileSaz6);
        TdKit fileKitSaz6 = new TdKit(new Td8Info(), saz6Bytes);
        Assert.assertEquals(kitSaz6, fileKitSaz6);
        Assert.assertTrue(kitSaz6.hashCode() == fileKitSaz6.hashCode(),
                "td10_7_tr808.hashCode=" + kitSaz6.hashCode() + " td10_7_tr808.hashCode="
                        + fileKitSaz6.hashCode());
    }
}
