package ui.web.base;

import com.aventstack.extentreports.ExtentReports;
import commons.*;
import org.openqa.selenium.WebDriver;
import ui.web.utilities.DriverUtil;
import java.util.*;

public class TestBase {
    private List<List<String>> testData;
    private List<List<String>> scenarioData;
    private Properties properties;

    public ExtentReports getReports(){
        return ExtentUtil.initializeReport();
    }

    public WebDriver getDriver(){
        DriverUtil.initializeDriver(get("browser"),Boolean.parseBoolean(get("headless")));
        return DriverUtil.getDriver();
    }

    public void setTestData(String filePath){
        if(filePath == null || filePath.isBlank()){
            LogUtil.logError(this.getClass(), String.format("Error attempting to set test data. '%s' is not a valid filePath.", filePath));
            throw new IllegalArgumentException(String.format("'%s' is not a valid filePath.", filePath));
        }
        testData = FileUtil.getTestData(filePath);
    }

    public void setProperties(String filePath){
        if(filePath == null || filePath.isBlank()){
            LogUtil.logError(this.getClass(), String.format("Error attempting to set test data. '%s' is not a valid filePath.", filePath));
            throw new IllegalArgumentException(String.format("'%s' is not a valid filePath.", filePath));
        }
        properties = FileUtil.getProperties(filePath);
    }

    public String get(String key){
        if(properties == null){
            LogUtil.logError(this.getClass(),String.format("Couldn't retrieve '%s' because properties == null. setProperties(String) first.", key));
            throw new IllegalStateException(String.format("Couldn't retrieve '%s' because properties == null.", key));
        }
        return properties.getProperty(key);
    }

    public List<List<String>> setScenarioData(String scenarioTitle){
        scenarioData = new ArrayList<>();
        if(testData == null){
            LogUtil.logError(this.getClass(),"Couldn't retrieve data because test data file is undefined. setTestData(String) first.");
            throw new IllegalStateException("Couldn't retrieve data because test data file is undefined.");
        }
        List<String> headings = testData.get(0);
        for(int i = 1; i < testData.size();i ++){
            List<String> dataRow = testData.get(i);
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
        List<String> headings = testData.get(0);
        List<String> currRow = scenarioData.get(row);
        int indexOfTargetField = headings.indexOf(targetField);
        return currRow.get(indexOfTargetField);
    }

}
