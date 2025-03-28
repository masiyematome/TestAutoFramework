package api.base;

import api.utilities.ApiUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import commons.ExtentUtil;
import commons.FileUtil;
import commons.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Getter
public class TestBase {
    private List<List<String>> scenarioData;
    private Properties properties;
    private List<List<String>> csvData;
    private String csvDataFilePath;

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

    public List<List<String>> setScenarioData(String scenarioTitle){
        scenarioData = new ArrayList<>();
        if(csvData == null){
            LogUtil.logError(this.getClass(),"Couldn't retrieve data because test data file is undefined. setTestData(String) first.");
            throw new IllegalStateException("Couldn't retrieve data because test data file is undefined.");
        }
        List<String> headings = csvData.get(0);
        for(int i = 1; i < csvData.size();i ++){
            List<String> dataRow = csvData.get(i);
            if(dataRow.get(headings.indexOf("test_description")).equalsIgnoreCase(scenarioTitle.trim())){
                scenarioData.add(dataRow);
            }
        }
        return scenarioData;
    }

    public int getTestDataNumRows(){
        if(scenarioData == null){
            throw new IllegalStateException("Cannot get size of scenario data because scenario data is null / not set.");
        }
        return scenarioData.size();
    }

    public String get(String scenarioTitle,String targetField, int row){
        List<List<String>> scenarioData = setScenarioData(scenarioTitle);
        List<String> headings = csvData.get(0);
        List<String> currRow = scenarioData.get(row);
        int indexOfTargetField = headings.indexOf(targetField);
        return currRow.get(indexOfTargetField);
    }
    
}
