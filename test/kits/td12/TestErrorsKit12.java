package kits.td12;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.midi.InvalidMidiDataException;

import kits.TdKit;
import kits.info.Td12Info;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import resources.UtilsForTests;
import exceptions.NotRolandException;
import exceptions.VdrumException;

@Test(groups = { "exception12" })
public class TestErrorsKit12 {
    @Test(expectedExceptions = InvalidMidiDataException.class)
    public void checkBadStatus() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File fileBadStat = UtilsForTests.getFile("maple3BadStat.syx");
        byte[] kitBytes = FileUtils.readFileToByteArray(fileBadStat);
        new TdKit(new Td12Info(), kitBytes);
    }

//    @Test(expectedExceptions = BadChecksumException.class)
//    public void checkBadChecksum() throws URISyntaxException, IOException,
//            InvalidMidiDataException, VdrumException {
//        final File fileBadStat = UtilsForTests.getFile("maple3BadChecksum.syx");
//        byte[] kitBytes = FileUtils.readFileToByteArray(fileBadStat);
//        new TdKit(new Td12Info(),kitBytes);
//    }

//    public void checkBadChecksumMessage() throws URISyntaxException, IOException,
//            InvalidMidiDataException, VdrumException {
//        final File fileBadStat = UtilsForTests.getFile("maple3BadChecksum.syx");
//        byte[] kitBytes = FileUtils.readFileToByteArray(fileBadStat);
//        try {
//            new TdKit(new Td12Info(),kitBytes);
//            Assert.fail();
//        }
//        catch (BadChecksumException e) {
//            Assert.assertEquals("Bad checksum in message", e.getProblem());
//        }
//    }

    @Test(expectedExceptions = NotRolandException.class)
    public void checkNotRoland() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File fileBadStat = UtilsForTests.getFile("maple3NotRoland.syx");
        byte[] kitBytes = FileUtils.readFileToByteArray(fileBadStat);
        new TdKit(new Td12Info(),kitBytes);
    }

    public void checkNotRolandMessage() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File fileBadStat = UtilsForTests.getFile("maple3NotRoland.syx");
        byte[] kitBytes = FileUtils.readFileToByteArray(fileBadStat);
        try {
            new TdKit(new Td12Info(),kitBytes);
            Assert.fail();
        }
        catch (NotRolandException e) {
            Assert.assertEquals("Not a Roland product", e.getProblem());
        }
    }

//    @Test(expectedExceptions = BadMessageLengthException.class)
//    public void checkBadLength() throws URISyntaxException, IOException,
//            InvalidMidiDataException, VdrumException {
//        final File fileBadStat = UtilsForTests.getFile("myOrigKits.syx");
//        byte[] kitBytes = FileUtils.readFileToByteArray(fileBadStat);
//        new TdKit(new Td12Info(),kitBytes);
//    }

//    public void checkBadLengthMessage() throws URISyntaxException, IOException,
//            InvalidMidiDataException, VdrumException {
//        final File fileBadStat = UtilsForTests.getFile("myOrigKits.syx");
//        byte[] kitBytes = FileUtils.readFileToByteArray(fileBadStat);
//        try {
//            new TdKit(new Td12Info(),kitBytes);
//            Assert.fail();
//        }
//        catch (BadMessageLengthException e) {
//            Assert.assertEquals("Bad message length", e.getProblem());
//        }
//    }
}
