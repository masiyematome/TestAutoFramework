package api.tests;

import api.constants.HttpMethod;
import api.utilities.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.io.File;
import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FWUtilTest{

    @BeforeAll
    public void setup(){
        LogUtil.logInfo(this, "Setting up for tests");
        String propertiesFile = "src/test/resources/config.properties";
        String csvDataFile = "src/test/resources/data/api-test.csv";
        FileUtil.initialize(propertiesFile, csvDataFile);
        RestAssured.baseURI = FileUtil.getInstance().getJsonPHBaseURI();
    }

    @BeforeEach
    public void setupForEach(TestInfo testInfo){
        LogUtil.logInfo(this,"starting test '" + testInfo.getDisplayName() + "'");
    }

    @Test
    @DisplayName("GET data")
    public void getData(TestInfo testInfo) {
        String endPoint = "/users";
        Map<String, String> params = new HashMap<>();
        Response response = ApiUtil.sendRequest(HttpMethod.GET,endPoint, "", params);
        response.prettyPrint();
        LogUtil.logInfo(this,"'" + testInfo.getDisplayName() + "' completed...");
    }

    @Test
    @DisplayName("CREATE a Resource")
    public void createAResource(TestInfo testInfo){
        String endPoint = "/users";
        String requestBody = """
                {
                  "name": "Matt",
                  "job": "SDET"
                }
                """;
        Map<String, String> params = new HashMap<>();
        params.put("header_Content-Type","application/json");
        Response response = ApiUtil.sendRequest(HttpMethod.POST, endPoint, requestBody, params);
        response.prettyPrint();
        LogUtil.logInfo(this,"'" + testInfo.getDisplayName() + "' completed...");
    }

    @Test
    @DisplayName("UPDATE a Resource")
    public void updateAResource(TestInfo testInfo){
        String endPoint = "/users/{id}";
        String requestBody = """
                {
                  "name": "Matt",
                  "job": "Senior SDET"
                }
                """;
        Map<String, String> params = new HashMap<>();
        params.put("header_Content-Type","application/json");
        params.put("path_id","1");
        Response response = ApiUtil.sendRequest(HttpMethod.PUT, endPoint, requestBody, params);
        response.prettyPrint();
        LogUtil.logInfo(this,"'" + testInfo.getDisplayName() + "' completed...");
    }

    @Test
    @DisplayName("DELETE a Resource")
    public void deleteAResource(TestInfo testInfo){
        String endPoint = "/users/{id}";
        Map<String, String> params = new HashMap<>();
        params.put("path_id","2");
        Response response = ApiUtil.sendRequest(HttpMethod.DELETE, endPoint, "", params);
        response.print();
        assertEquals(HttpStatus.SC_OK, response.statusCode());
        LogUtil.logInfo(this,"'" + testInfo.getDisplayName() + "' completed...");
    }
}
