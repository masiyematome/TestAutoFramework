package api.tests;

import api.constants.HttpMethod;
import api.utilities.ApiUtil;
import api.utilities.FileUtil;
import api.utilities.LogUtil;
import api.utilities.TestBase;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SOAP extends TestBase {

    private Map<String, Object> params;
    private ExtentReports reports;

    @BeforeAll
    public void setup(){
        setProperties("src/test/resources/config.properties");
        setTestData("src/test/resources/data/soap-api-scenarios/get-country-cap-city.csv");
        reports = initializeReport();
        params = new HashMap<>();
        setBaseURI("capCityBaseURI");
    }

    @BeforeEach
    public void setupForEach(TestInfo info){
        params.clear();
        LogUtil.logInfo(this.getClass(), "test '" + info.getDisplayName() + "' started...");
    }

    @AfterEach
    public void wrapUpForEach(TestInfo info){
        LogUtil.logInfo(this.getClass(), "test '" + info.getDisplayName() + "' done");
    }

    @Test
    @DisplayName("Get Capital City for a Country")
    public void getCapCity(TestInfo info){
        ExtentTest test = reports.createTest(info.getDisplayName());
        String requestBody = FileUtil.getDataFromFile("src/test/resources/data/soap-request-messages/country-info-service.xml");
        params.put("header_Content-Type","text/xml");
        Response response=null;
        for(int i = 1; i < getCsvNumRows(); i++){
            ExtentTest node = test.createNode(get("country",i));
            try{
                response = ApiUtil.sendRequest(HttpMethod.POST, "", requestBody.replace("${ISO-CODE}",get("iso_code",i)),params);
                String capCity = response.xmlPath().getString("Envelope.Body.CapitalCityResponse.CapitalCityResult");
                assertEquals(get("expected_SC",i),String.valueOf(response.statusCode()));
                assertEquals(get("capital_city", i),capCity);
                report(node,"pass","Successfully fetched the capital city for " + get("country", i), response);
            }catch (Exception | Error e){
                LogUtil.logError(this.getClass(), e.getMessage());
                report(node,"fail","Failed to fetch the capital city for " + get("country", i), response);
                throw e;
            }
        }
    }

    @AfterAll
    public void wrapUp(){
        reports.flush();
    }

}
