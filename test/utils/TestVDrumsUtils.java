package utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import resources.UtilsForTests;

public class TestVDrumsUtils {
    @Test(groups = { "functest", "checkintest" })
    public void checkCalculateChecksum() throws URISyntaxException {
        final File file = UtilsForTests.getFile("maple4.syx");
        try {
            final byte[] fileBytes = FileUtils.readFileToByteArray(file);
            final int calculatedChecksum = calculateChecksum(Arrays.copyOfRange(fileBytes,
                    1840, 1972));
            final int actChecksum = fileBytes[1972];
            Assert.assertTrue(calculatedChecksum == actChecksum, "calculatedChecksum="
                    + calculatedChecksum + " actChecksum=" + actChecksum);
        }
        catch (IOException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test(groups = { "functest", "checkintest" })
    public void checkCalculateChecksum1() throws URISyntaxException {
        final File file = UtilsForTests.getFile("maple4.syx");
        try {
            final byte[] fileBytes = FileUtils.readFileToByteArray(file);
            final int calculatedChecksum = calculateChecksum(Arrays.copyOfRange(fileBytes, 7,
                    139));
            final int actChecksum = fileBytes[139];
            Assert.assertTrue(calculatedChecksum == actChecksum, "calculatedChecksum="
                    + calculatedChecksum + " actChecksum=" + actChecksum);
        }
        catch (IOException e) {
            Assert.fail(e.getMessage());
        }

    }

    /**
     * Calculates sum according to TD-12 MIDI Implementation (p.12) sum of bytes sum mod 128 =
     * reminder 128 - reminder = checksum
     * 
     * @param data
     *            byte[]
     * @return
     */
    private static int calculateChecksum(final byte[] data) {
        int sum = 0;
        for (int i = 0; i < data.length; i++) {
            int currentInt = (int) data[i] & 0xFF;
            sum += currentInt;
        }
        final int reminder = sum % 128;
        if (reminder == 0) {
            return 0;
        }
        final int checksum = 128 - reminder;
        return checksum;
    }
}
