package api.tests;

import api.base.TestBase;
import api.constants.HttpMethod;
import api.utilities.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import commons.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GoogleDriveTests extends TestBase {

    private File file;
    private final Map<String, Object> params = new HashMap<>();
    private String fileName;
    private String fileId;
    private Response response;
    private ExtentReports reports;
    private String newName;
    private ExtentTest test;

    @BeforeAll
    public void setup() throws IOException {
        String serviceAccountDetailsEncrypted = "src/test/resources/api/service-account-details.json";
        String serviceAccountDetailsDecrypted = "src/test/resources/api/service-account-details-decrypted.json";
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(System.getenv("AES_KEY")),"AES");

        file = new File(serviceAccountDetailsDecrypted);
        FileWriter writer = new FileWriter(file,false);
        writer.write(SecurityUtil.decrypt(FileUtil.getDataFromFile(serviceAccountDetailsEncrypted),"AES", secretKey));
        writer.close();

        setProperties("src/test/resources/api/config.properties");
        setBaseURI("googleBaseURI");
        reports = initializeReport();

        fileName = "test_sample_file_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".pdf";
    }

    @BeforeEach
    public void setupForEach(TestInfo info){
        test = reports.createTest(info.getDisplayName());
        LogUtil.logInfo(this.getClass(), "Starting test '" + info.getDisplayName() + "'.");
        params.clear();
        params.put("header_Authorization","Bearer " + getAccessToken());
    }

    @Test
    @DisplayName("Verify a user can upload a file to google drive.")
    @Order(1)
    public void uploadFile(TestInfo info){
        ExtentTest node = test.createNode("Uploading file: " + fileName);
        try{
            String endPoint = "/upload/drive/v3/files?uploadType=multipart";
            String metaData = String.format("""
                {
                    "name": "%s",
                    "mimeType": "application/pdf",
                    "parents": ["%s"],
                    "description": "This is just a test file"
                }
                """, fileName, get("googleFolderId"));
            params.put("multipart_file",new File("src/test/resources/api/files/sample_PDF_1MB.pdf"));
            params.put("multipart_metadata", metaData);

            response = ApiUtil.sendFile(HttpMethod.POST, endPoint, params);
            assertThat(response.statusCode()).isEqualTo(200);
            fileId = response.jsonPath().getString("id");
            assertNotNull(fileId);
            report(node,"pass","Successfully uploaded file to google drive.", response);
        }catch (Exception | Error e){
            LogUtil.logError(this.getClass(), "'" + info.getDisplayName() + "' failed. " + e.getMessage());
            report(node,"fail","Failed while uploading file to google drive.", response);
            throw e;
        }finally {
            response.prettyPrint();
        }
    }

    @Test
    @DisplayName("Verify user can view uploaded files")
    @Order(2)
    public void getFile(TestInfo info){
        ExtentTest node = test.createNode("Retrieving file: " + fileName);
        try{
            String endPoint = "/drive/v3/files/{fileId}";
            params.put("path_fileId", fileId);
            response = ApiUtil.sendRequest(HttpMethod.GET,endPoint,"",params);
            String retrievedFileName = response.jsonPath().getString("name");
            assertThat(retrievedFileName).isEqualTo(fileName);
            report(node,"pass","Successfully retrieved file from google drive.", response);
        }catch (Exception | Error e){
            LogUtil.logError(this.getClass(), "'" + info.getDisplayName() + "' failed. " + e.getMessage());
            report(node,"fail","Failed while retrieving file from google drive.", response);
            throw e;
        }finally {
            response.prettyPrint();
        }
    }

    @Test
    @DisplayName("Verify a user can rename a file")
    @Order(3)
    public void renameFile(TestInfo info){
        newName = "newName_" + fileName;
        ExtentTest node = test.createNode("Renaming  file '" + fileName + "' to '" + newName + "'.");
        try{
            String endPoint = "/drive/v3/files/{fileId}";
            String updateInfo = String.format("""
                    {
                        "name":"%s"
                    }
                """, newName);
            params.put("path_fileId", fileId);

            response = ApiUtil.sendRequest(HttpMethod.PATCH, endPoint,updateInfo, params);
            assertThat(response.statusCode()).isEqualTo(200);
            response = ApiUtil.sendRequest(HttpMethod.GET,endPoint,"",params);
            String retrievedFileName = response.jsonPath().getString("name");
            assertThat(retrievedFileName).isEqualTo(newName);
            report(node,"pass","Successfully renamed file on google drive.", response);
        }catch (Exception | Error e){
            LogUtil.logError(this.getClass(), "'" + info.getDisplayName() + "' failed. " + e.getMessage());
            report(node,"fail","Failed while renaming file on google drive.", response);
            throw e;
        }finally {
            response.prettyPrint();
        }
    }

    @Test
    @DisplayName("Verify a user can delete a file")
    @Order(4)
    public void deleteFile(TestInfo info){
        ExtentTest node = test.createNode("Deleting file '" + newName + "'.");
        try{
            String endPoint = "/drive/v3/files/{fileId}";
            params.put("path_fileId", fileId);
            response = ApiUtil.sendRequest(HttpMethod.DELETE, endPoint,"", params);
            assertThat(response.statusCode()).isEqualTo(204);
            report(node,"pass","Successfully deleted file on google drive.", response);
        }catch (Exception | Error e){
            LogUtil.logError(this.getClass(), "'" + info.getDisplayName() + "' failed. " + e.getMessage());
            report(node,"fail","Failed while deleting file on google drive.", response);
            throw e;
        }finally {
            response.prettyPrint();
        }
    }


    @AfterAll
    public void tearDown(){
        if(file != null){
            boolean isDeleted = file.delete();
            assertThat(isDeleted).isTrue();
        }
        reports.flush();
    }
}
