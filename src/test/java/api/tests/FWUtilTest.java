package api.tests;

import api.constants.HttpMethod;
import api.utilities.*;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FWUtilTest{

    @BeforeAll
    public void setup(){
        LogHelper.logInfo(this, "Setting up for tests");
        String propertiesFile = "src/test/resources/config.properties";
        String csvDataFile = "src/test/resources/data/api-test.csv";
        FileUtil.initialize(propertiesFile, csvDataFile);
    }

    @BeforeEach
    public void setupForEach(TestInfo testInfo){
        LogHelper.logInfo(this,"starting test '" + testInfo.getDisplayName() + "'");
    }

    @Test
    @DisplayName("GET data")
    public void getData(TestInfo testInfo) {
        String endPoint = "/users";
        Map<String, String> params = new HashMap<>();
        Response response = ApiHelper.sendRequest(HttpMethod.GET,endPoint, "", params);
        response.prettyPrint();
        LogHelper.logInfo(this,"'" + testInfo.getDisplayName() + "' completed...");
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
        Response response = ApiHelper.sendRequest(HttpMethod.POST, endPoint, requestBody, params);
        response.prettyPrint();
        LogHelper.logInfo(this,"'" + testInfo.getDisplayName() + "' completed...");
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
        params.put("path_id","123");
        Response response = ApiHelper.sendRequest(HttpMethod.PUT, endPoint, requestBody, params);
        response.prettyPrint();
        LogHelper.logInfo(this,"'" + testInfo.getDisplayName() + "' completed...");
    }

    @Test
    @DisplayName("DELETE a Resource")
    public void deleteAResource(TestInfo testInfo){
        String endPoint = "/users/{id}";
        Map<String, String> params = new HashMap<>();
        params.put("path_id","2");
        Response response = ApiHelper.sendRequest(HttpMethod.DELETE, endPoint, "", params);
        response.print();
        assertEquals(HttpStatus.SC_NO_CONTENT, response.statusCode());
        LogHelper.logInfo(this,"'" + testInfo.getDisplayName() + "' completed...");
    }

}
