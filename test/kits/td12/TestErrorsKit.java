package kits.td12;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.midi.InvalidMidiDataException;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import resources.Utils;
import exceptions.BadChecksumException;
import exceptions.BadMessageLengthException;
import exceptions.NotRolandException;
import exceptions.VdrumException;

@Test(groups = { "exception" })
public class TestErrorsKit {
    @Test(expectedExceptions = InvalidMidiDataException.class)
    public void checkBadStatus() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File fileBadStat = Utils.getTestFile("maple3BadStat.syx");
        byte[] kitBytes = FileUtils.readFileToByteArray(fileBadStat);
        new TD12Kit(kitBytes);
    }

    @Test(expectedExceptions = BadChecksumException.class)
    public void checkBadChecksum() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File fileBadStat = Utils.getTestFile("maple3BadChecksum.syx");
        byte[] kitBytes = FileUtils.readFileToByteArray(fileBadStat);
        new TD12Kit(kitBytes);
    }

    @Test(expectedExceptions = NotRolandException.class)
    public void checkNotRoland() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File fileBadStat = Utils.getTestFile("maple3NotRoland.syx");
        byte[] kitBytes = FileUtils.readFileToByteArray(fileBadStat);
        new TD12Kit(kitBytes);
    }

    @Test(expectedExceptions = BadMessageLengthException.class)
    public void checkBadLength() throws URISyntaxException, IOException,
            InvalidMidiDataException, VdrumException {
        final File fileBadStat = Utils.getTestFile("myOrigKits.syx");
        byte[] kitBytes = FileUtils.readFileToByteArray(fileBadStat);
        new TD12Kit(kitBytes);
    }
}
