package api.utilities;

import io.restassured.RestAssured;

import java.util.Properties;

public class TestBase {
    private Properties properties;
    private String propertiesFilePath;

    public void setProperties(String propertiesFilePath){
        this.properties = FileUtil.getProperties(propertiesFilePath);
        this.propertiesFilePath = propertiesFilePath;
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

        return properties.getProperty(property);
    }

    public void setBaseURI(String key){
        RestAssured.baseURI = get(key);
    }

    public String getAccessToken(){
        return ApiUtil.getAccessToken(get("googleAuthKeyPath"));
    }

}
