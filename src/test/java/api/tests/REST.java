package api.tests;

import api.constants.HttpMethod;
import api.utilities.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import commons.LogUtil;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class REST extends TestBase {

    private Response response;
    private Map<String, Object> params;
    private ExtentReports reports;

    @BeforeAll
    public void setup(){
        setProperties("src/test/resources/api/config.properties");
        setTestData("src/test/resources/api/data/get-api-scenarios/get-lyrics-api.csv");
        reports = initializeReport();
        params = new HashMap<>();
    }

    @BeforeEach
    public void setupForEach(TestInfo testInfo){
        LogUtil.logInfo(this.getClass(),"Starting test '"+testInfo.getDisplayName()+"'. ");
        params.clear();
    }

    @AfterEach
    public void wrapUpForEach(TestInfo testInfo){
        LogUtil.logInfo(this.getClass(),"test '"+testInfo.getDisplayName()+"' done.");
    }

    @Test
    @DisplayName("Search for the lyrics of an artist and song title.")
    public void searchForLyrics(TestInfo testInfo){
        ExtentTest test = reports.createTest(testInfo.getDisplayName());
        setBaseURI("musicAPI");
        for(int i = 1; i < getCsvNumRows(); i++){
            String songTitle = get("song_title",i), songArtist = get("song_artist",i);
            params.put("path_artist",songArtist);
            params.put("path_title",songTitle);
            try{
                response = ApiUtil.sendRequest(HttpMethod.GET, get("endPoint", i), "",params);
                assertEquals(get("expected_SC", i),String.valueOf(response.statusCode()));
                report(test.createNode(get("test_description", i)),"pass","", response);
            }catch (Exception | Error e){
                LogUtil.logError(this.getClass(), e.getMessage());
                report(test.createNode(get("test_description", i)),"fail","Lyric search was unsuccessful", response);
                throw e;
            }
        }
    }

    @AfterAll
    public void wrapUp(){
        reports.flush();
    }

}
