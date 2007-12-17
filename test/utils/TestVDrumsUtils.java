package utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import resources.Utils;

public class TestVDrumsUtils {
    @Test(groups = { "functest", "checkintest" })
    public void checkCalculateChecksum() throws URISyntaxException {
        final File file = Utils.getFile("maple4.syx");
        try {
            final byte[] fileBytes = FileUtils.readFileToByteArray(file);
            final int calculatedChecksum = VDrumsUtils.calculateChecksum(Arrays
                    .copyOfRange(fileBytes, 1840, 1972));
            final int actChecksum = fileBytes[1972];
            Assert
                    .assertTrue(calculatedChecksum == actChecksum,
                            "calculatedChecksum=" + calculatedChecksum + " actChecksum="
                                    + actChecksum);
        }
        catch (IOException e) {
            Assert.fail(e.getMessage());
        }

    }
    @Test(groups = { "functest", "checkintest" })
    public void checkCalculateChecksum1() throws URISyntaxException {
        final File file = Utils.getFile("maple4.syx");
        try {
            final byte[] fileBytes = FileUtils.readFileToByteArray(file);
            final int calculatedChecksum = VDrumsUtils.calculateChecksum(Arrays
                    .copyOfRange(fileBytes, 7, 139));
            final int actChecksum = fileBytes[139];
            Assert
                    .assertTrue(calculatedChecksum == actChecksum,
                            "calculatedChecksum=" + calculatedChecksum + " actChecksum="
                                    + actChecksum);
        }
        catch (IOException e) {
            Assert.fail(e.getMessage());
        }

    }
}
