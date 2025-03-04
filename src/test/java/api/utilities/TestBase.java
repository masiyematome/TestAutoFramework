package api.utilities;

import io.restassured.RestAssured;
import lombok.*;
import java.util.*;

@Setter
@Getter
public class TestBase {
    private Properties properties;
    private List<List<String>> csvData;

    public TestBase(String propertiesFile, String csvDataFile){
        setDataForTest(propertiesFile, csvDataFile);
    }

    private void setDataForTest(String propertiesFile, String csvDataFile){
        FileUtil.initialize(propertiesFile, csvDataFile);
        FileUtil fileUtil = FileUtil.getInstance();
        properties = fileUtil.getProperties();
        csvData = fileUtil.getCsvData();
    }

    public void setBaseURI(){
        if(properties == null) {
            throw new NullPointerException("Cannot load property 'baseURI' because properties=null");
        }
        RestAssured.baseURI = properties.getProperty("baseURI");
        LogUtil.logInfo(this, "Base Url set: " + RestAssured.baseURI);
    }

    public String getApiKey(){
        return System.getenv("API_KEY");
    }

    public List<List<String>> getCsvData(){
        if(csvData == null) {
            throw new NullPointerException("Couldn't retrieve csv test data - no data was loaded into the csvData list");
        }
        return csvData;
    }

}
