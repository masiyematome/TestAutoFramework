package api.tests;

import api.constants.HttpMethod;
import api.utilities.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WeatherApiTest {
    private TestBase testBase;
    @BeforeAll
    public void setupForAll(){
        String propertiesFile = "src/test/resources/config.properties";
        String csvDataFile = "";
        testBase = new TestBase(propertiesFile, csvDataFile);
        testBase.setBaseURI();
    }

    @Test
    @DisplayName("Fetch current weather by city name")
    public void fetchCurrentWeather(){
        String endPoint = "/data/2.5/weather";
        Map<String, String> params = new HashMap<>();
        params.put("query_q","London");
        params.put("query_appid", testBase.getApiKey());
        Response response = ApiUtil.sendRequest(HttpMethod.GET, endPoint, "",params);
        response.prettyPrint();
    }

}
