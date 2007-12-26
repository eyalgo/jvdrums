package kits.td12;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import resources.UtilsForTests;
import exceptions.VdrumException;

@Test(groups = {"kits12"})
public final class TestKit12 {
    //test commit
    private TD12Kit kit1;
    private TD12Kit kit25;
    private SysexMessage kit1SysexMessage;

    @BeforeMethod
    public void setUp() throws IOException, InvalidMidiDataException, URISyntaxException,
            VdrumException {
        final File file1 = UtilsForTests.getFile("maple1.syx");
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        kit1 = new TD12Kit(kitBytes1);
        final File file25 = UtilsForTests.getFile("Bubbles25.syx");
        byte[] kitBytes25 = FileUtils.readFileToByteArray(file25);
        kit25 = new TD12Kit(kitBytes25);
        kit1SysexMessage = new SysexMessage();
        kit1SysexMessage.setMessage(kitBytes1, kitBytes1.length);
    }
    
    public void checkRawDataSyxMessage() throws URISyntaxException, IOException, InvalidMidiDataException, VdrumException {
        final File file1 = UtilsForTests.getFile("maple1.syx");
        byte[] kitBytes1 = FileUtils.readFileToByteArray(file1);
        TD12Kit kitRaw = new TD12Kit(kitBytes1);
        
        SysexMessage message = new SysexMessage();
        message.setMessage(kitBytes1, kitBytes1.length);
        
        TD12Kit kitMessage = new TD12Kit(message);
        Assert.assertEquals(kitMessage, kitRaw);
    }

    public void checkIdKit_1() {
        Assert.assertTrue(kit1.getId() == 1, "TD12Kit 1 with id 1");
    }

    public void checkNameKit_1() {
        Assert.assertEquals("Maple", kit1.getName(), "Kit1 should be Maple");
    }

    public void checkIdKit_25() {
        Assert.assertTrue(kit25.getId() == 25, "TD12Kit 25 with id 25");
    }

    public void checkNameKit_25() {
        Assert.assertEquals("Bubbles", kit25.getName(), "Kit25 should be Bubbles");
    }

    public void checkNewId() {
        try {
            TD12Kit kit32 = kit1.setNewId(32);
            Assert.assertTrue(kit32.getId() == 32, "TD12Kit 32 with id 32");
            Assert.assertTrue(kit1.getId() == 1,
                    "Check that the ID has not changed. TD12Kit 1 with id 1 (got "
                            + kit1.getId() + ")");
        }
        catch (InvalidMidiDataException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void checkMessageOfKit1() {
        try {
            Assert.assertEquals(kit1SysexMessage.getMessage(), kit1.getMessage().getMessage(),
                    "Kit1 message");
        }
        catch (InvalidMidiDataException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void checkBadId51() {
        try {
            kit1.setNewId(51);
        }
        catch (InvalidMidiDataException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void checkEqualsAftterNewId() throws IOException, InvalidMidiDataException,
            URISyntaxException, VdrumException {
        File fileMaple3 = UtilsForTests.getFile("maple3.syx");
        byte[] maple3Bytes = FileUtils.readFileToByteArray(fileMaple3);
        TD12Kit kitMaple3 = new TD12Kit(maple3Bytes);
        TD12Kit kitMaple4 = kitMaple3.setNewId(4);
        File fileMaple4 = UtilsForTests.getFile("maple4.syx");
        byte[] maple4Bytes = FileUtils.readFileToByteArray(fileMaple4);
        TD12Kit fileKitMaple4 = new TD12Kit(maple4Bytes);
        Assert.assertEquals(kitMaple4, fileKitMaple4);
    }

    public void checkHashAftterNewId() throws IOException, InvalidMidiDataException,
            URISyntaxException, VdrumException {
        File fileMaple3 = UtilsForTests.getFile("maple3.syx");
        byte[] maple3Bytes = FileUtils.readFileToByteArray(fileMaple3);
        TD12Kit kitMaple3 = new TD12Kit(maple3Bytes);
        TD12Kit kitMaple4 = kitMaple3.setNewId(4);
        File fileMaple4 = UtilsForTests.getFile("maple4.syx");
        byte[] maple4Bytes = FileUtils.readFileToByteArray(fileMaple4);
        TD12Kit fileKitMaple4 = new TD12Kit(maple4Bytes);
        Assert.assertTrue(kitMaple4.hashCode() == fileKitMaple4.hashCode(),
                "kitMaple4.hashCode=" + kitMaple4.hashCode() + " fileKitMaple4.hashCode="
                        + fileKitMaple4.hashCode());
    }
}
