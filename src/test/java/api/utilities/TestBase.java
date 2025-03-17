package api.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import commons.ExtentUtil;
import commons.FileUtil;
import commons.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Getter;

import java.util.List;
import java.util.Properties;

@Getter
public class TestBase {
    private Properties properties;
    private List<List<String>> csvData;
    private String csvDataFilePath;
    private short csvNumRows;
    private RuntimeException e;

    public void setProperties(String propertiesFilePath){
        this.properties = FileUtil.getProperties(propertiesFilePath);
    }

    public void setTestData(String testDataFilePath){
        this.csvData = FileUtil.getTestData(testDataFilePath);
        this.csvDataFilePath = testDataFilePath;
    }

    public String get(String property){
        String propertyValue;
        if(properties == null){
            LogUtil.logError(this.getClass(),"Couldn't get target property '" + property + "' because properties=null. Call setProperties first.");
            throw new RuntimeException("Couldn't get target property '" + property + "' because properties=null. Call setProperties first.");
        }

        if(property.isBlank()){
            LogUtil.logError(this.getClass(), "Couldn't fetch blank / null c'" + property + "'");
            throw new RuntimeException("Couldn't fetch blank / null property '" + property + "'.");
        }

        propertyValue = properties.getProperty(property);
        LogUtil.logInfo(this.getClass(), "Retrieved property '" + property + "': " + propertyValue);

        return propertyValue;
    }

    public void setBaseURI(String key){
        RestAssured.baseURI = get(key);
    }

    public String getAccessToken(){
        return ApiUtil.getAccessToken(get("googleAuthKeyPath"));
    }

    public ExtentReports initializeReport(){
        return ExtentUtil
                .initializeReport();
    }

    public void report(ExtentTest node, String status, String message, Response response){
        ExtentUtil.report(node,status,message,response);
    }

    public String get(String field, int row){
        if(csvData == null || csvData.isEmpty()){
            LogUtil.logError(this.getClass(),"Csv data is either null or empty. Call setTestData first.");
            throw new RuntimeException("Csv data is either null or empty. Call setTestData first.");
        }
        String fieldValue="";
        List<List<String>> data = csvData;
        List<String> currList = data.get(row);
        int indexOfTarget = data.get(0).indexOf(field);
        if(indexOfTarget == -1){
            LogUtil.logError(this.getClass(),"'" + field + "' is not a valid field in the csv file '" + csvDataFilePath + "'");
            throw new IllegalArgumentException("'" + field + "' is not a valid field in the csv file '" + csvDataFilePath + "'");
        }
        fieldValue = currList.get(indexOfTarget);
        return fieldValue;
    }

    public short getCsvNumRows(){
        if(csvData == null){
            LogUtil.logError(this.getClass(),"Csv data is null. Call setTestData first.");
            throw new RuntimeException("Csv data is null. Call setTestData first.");
        }

        return (short) csvData.size();
    }
    
}
