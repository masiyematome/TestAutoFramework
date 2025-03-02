package api.tests;

import api.constants.HttpMethod;
import api.security.Doppler;
import api.security.ReadKey;
import api.security.SecurityHandler;
import api.utilities.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import javax.crypto.SecretKey;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthTests {

    protected FileUtil fileUtil;

    @BeforeAll
    public void setup(){
        String propertiesPath = "src/test/resources/config.properties";
        FileUtil.initialize(propertiesPath,"");
        fileUtil = FileUtil.getInstance();
        RestAssured.baseURI = fileUtil.getBaseURI();
    }

    @Test
    @DisplayName("GET request with auth token test")
    public void getWithApiKey(TestInfo testInfo){
        LogUtil.logInfo(this, "Starting test: '" + testInfo.getDisplayName() + "' ");
        String endPoint = "/data/2.5/weather";
        Map<String, String> params = new HashMap<>();
        params.put("query_q", "London");
        params.put("query_appid", fileUtil.getApiKey());
        Response response = ApiUtil.sendRequest(HttpMethod.GET,endPoint,"",params);
        response.prettyPrint();
    }

    @Test
    @DisplayName("Just testing my encryption - decryption functionality")
    public void encryptDecrypt(){
        SecretKey key = ReadKey.readKey(new Doppler(), "AES_KEY");
        String data = "Matome Answer Masiye";
        String encryptedData = SecurityHandler.encrypt(key, data);
        String decryptedData = SecurityHandler.decrypt(key, encryptedData);
        System.out.printf("""
                data: %s,
                encryptedData: %s,
                decryptedData: %s,
                """, data, encryptedData, decryptedData);
    }
}
