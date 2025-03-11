package api.tests;

import api.constants.HttpMethod;
import api.utilities.*;
import com.google.gson.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GenTests extends TestBase{

    private String accessToken;
    private final String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

    @BeforeAll
    public void setup(){
        setProperties("src/test/resources/config.properties");
        setBaseURI("googleApiBaseURI");
        accessToken = getAccessToken();
    }

    @BeforeEach
    public void setupForEach(TestInfo testInfo){
        LogUtil.logInfo(this.getClass(),"Starting test '" + testInfo.getDisplayName() + "'.");
    }

    @AfterEach
    public void tearDownForEach(TestInfo testInfo){
        LogUtil.logInfo(this.getClass(), "'" + testInfo.getDisplayName() + "' done.");
    }

    @Test
    @Order(1)
    @DisplayName("Send a file via HTTP / HTTPS")
    public void sendAFileViaHTTP(){
        String filePath = "src/main/resources/fileInput.txt";
        String fieldToUpdate = String.format("""
                {
                    "name": "ApiTestFile_%s"
                }
                """,time);

        Map<String, Object> params = new HashMap<>();
        params.put("header_Content-Type","application/octet-stream");
        params.put("header_Authorization","Bearer " + accessToken);
        Response sendFileResponse = ApiUtil.sendFile(HttpMethod.POST, get("googleSendFileAPIEndpoint"), params, filePath);
        String fileId = sendFileResponse.jsonPath().getString("id");
        params.put("header_Content-Type","application/json");
        params.put("path_id", fileId);
        Response updateFileResponse = ApiUtil.sendRequest(HttpMethod.PATCH, get("googleUpdateFileAPIEndpoint"), fieldToUpdate,params);
        updateFileResponse.prettyPrint();
    }

    @Test
    @Order(2)
    @DisplayName("Get all files")
    public void getAllFiles(){
        String endPoint = "/drive/v3/files";
        Map<String, Object> params = new HashMap<>();
        params.put("header_Authorization", "Bearer " + accessToken);
        params.put("header_Accept", "application/json");
        Response response = ApiUtil.sendRequest(HttpMethod.GET, endPoint,"", params);
        response.prettyPrint();
    }

    @Test
    @Order(3)
    @DisplayName("Delete files by IDs")
    public void deleteFilesById(){
        String endPoint = "/drive/v3/files";
        Map<String, Object> params = new HashMap<>();
        params.put("header_Authorization", "Bearer " + accessToken);
        params.put("header_Accept", "application/json");
        Response response_get = ApiUtil.sendRequest(HttpMethod.GET, endPoint,"", params);
        JsonArray jsonArray = JsonParser.parseString(response_get.asString()).getAsJsonObject().getAsJsonArray("files");

        for(JsonElement object:jsonArray){
            if(object.getAsJsonObject().get("name").getAsString().equals("ApiTestFile_"+time)){
                String id = object.getAsJsonObject().get("id").getAsString();
                params.put("path_fileId", id);
                Response response = ApiUtil.sendRequest(HttpMethod.DELETE, endPoint + "/{fileId}", "", params);
                System.out.println("status code after deleting '" + id + "': " + response.statusCode());
                response.prettyPrint();
            }
        }
    }

}
