package api.tests;

import api.constants.HttpMethod;
import api.utilities.ApiUtil;
import api.base.TestBase;
import com.aventstack.extentreports.*;
import commons.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CountryInfoServiceTests extends TestBase {
    private final String endPoint;
    private Map<String, Object> params;
    private Response response;
    private ExtentReports reports;
    private ExtentTest test;
    private String soapMessageFilePath;

    public CountryInfoServiceTests() {
        endPoint = "/websamples.countryinfo/CountryInfoService.wso";
        params = new HashMap<>();
    }

    @BeforeAll
    public void setup() {
        setProperties("src/test/resources/api/config.properties");
        setTestData("src/test/resources/api/data/soap-api-scenarios/get-country-cap-city.csv");
        setBaseURI("countryInfoBaseURI");
        soapMessageFilePath = "src/test/resources/api/data/soap-request-messages/country-info-service.xml";
        reports = initializeReport();
    }

    @BeforeEach
    public void setupForEach(TestInfo info) {
        test = reports.createTest(info.getDisplayName());
        LogUtil.logInfo(this.getClass(), "Starting tests'" + info.getDisplayName() + "'.");
        params.clear();
        setScenarioData(info.getDisplayName());
    }

    @Test
    @DisplayName("Verify user can retrieve capital city.")
    public void getCountryInformation(TestInfo info) {
        params.put("header_Content-Type", "text/xml;charset=utf-8");
        for(int i = 0; i < getTestDataNumRows(); i++){
            String isoCode = get(info.getDisplayName(),"iso_code",i);
            String expectedCapCity = get(info.getDisplayName(),"capital_city_result",i);
            ExtentTest node = test.createNode("Retrieving capital city for '" + isoCode + "'.");
            try {
                String soapMessage = FileUtil.getDataFromFile(soapMessageFilePath).replace("${ISO-CODE}", isoCode);
                response = ApiUtil.sendRequest(HttpMethod.POST, endPoint, soapMessage, params);
                String capCity = response.xmlPath().getString("Envelope.Body.CapitalCityResponse.CapitalCityResult");
                assertThat(response.statusCode()).isEqualTo(200);
                assertThat(capCity).isEqualTo(expectedCapCity);
                report(node, "pass","Retrieved '" + capCity + "' as the capital city result.",response);
            }catch (Exception | Error e){
                LogUtil.logError(this.getClass(),"Failed to retrieve capital city. " + e.getMessage());
                report(node, "fail","Failed to retrieve capital city. ",response);
                throw e;
            }finally {
                if(response != null){
                    response.prettyPrint();
                }
            }
        }
    }

    @AfterAll
    public void tearDown(){
        reports.flush();
    }

}
