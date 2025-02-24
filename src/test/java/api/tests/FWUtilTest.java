package api.tests;

import api.utilities.FileUtil;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FWUtilTest{
    private static final Logger LOG = LoggerFactory.getLogger(FWUtilTest.class);
    @Test
    @DisplayName("Test used to test utility methods")
    public void testUtils(TestInfo testInfo){
        LOG.warn("The test '{}' will be removed once the utilities have been tested", testInfo.getDisplayName());
        String propertiesFile = "src/test/resources/config.properties";
        String csvDataFile = "src/test/resources/data/api-test.csv";
        FileUtil fileUtil = FileUtil.getInstance(propertiesFile, csvDataFile);
        List<List<String>> csvData = fileUtil.getCsvData();
        System.out.println(fileUtil.getTest());
        csvData.forEach(System.out::println);
    }
}
