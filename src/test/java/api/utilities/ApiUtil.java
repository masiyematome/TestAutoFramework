package api.utilities;

import api.constants.HttpMethod;
import com.google.auth.oauth2.GoogleCredentials;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;


public class ApiUtil {

    public static Response sendRequest(HttpMethod method, String endPoint, String requestBody, Map<String, Object> params){
        var request = setParams(params);
        Response response = switch (method){
            case GET -> request
                    .when()
                    .get(endPoint)
                    .then().extract().response();
            case POST -> request
                    .body(requestBody)
                    .when()
                    .post(endPoint)
                    .then().extract().response();
            case PUT -> request
                    .when()
                    .put(endPoint)
                    .then().extract().response();
            case DELETE -> request
                    .when()
                    .delete(endPoint)
                    .then().extract().response();
            case PATCH -> request
                    .body(requestBody)
                    .when()
                    .patch(endPoint)
                    .then().extract().response();
            default -> throw new UnsupportedOperationException("Method '" + method + "' unsupported.");
        };
        LogUtil.logInfo(ApiUtil.class, "Successfully made " + method.name() + " request to " + RestAssured.baseURI + endPoint);
        return response;
    }

    public static Response sendFile(HttpMethod method,String endPoint, Map<String, Object> params){
        var request = setParams(params);
        Response response = switch (method){
            case POST -> request
                    .when()
                    .post(endPoint)
                    .then().extract().response();
            case PUT -> request
                    .when()
                    .put(endPoint)
                    .then().extract().response();
            default -> throw new UnsupportedOperationException("Method '" + method + "' unsupported.");
        };
        LogUtil.logInfo(ApiUtil.class, "Successfully made " + method.name() + " request to " + RestAssured.baseURI + endPoint);
        return response;
    }

    public static Response sendFile(HttpMethod method,String endPoint, Map<String, Object> params, String filePath){
        var request = setParams(params);
        Response response = switch (method){
            case POST -> request
                    .body(FileUtil.readFileAsBytes(filePath))
                    .when()
                    .post(endPoint)
                    .then().extract().response();
            case PUT -> request
                    .body(FileUtil.readFileAsBytes(filePath))
                    .when()
                    .put(endPoint)
                    .then().extract().response();
            default -> throw new UnsupportedOperationException("Method '" + method + "' unsupported.");
        };
        LogUtil.logInfo(ApiUtil.class, "Successfully made " + method.name() + " request to " + RestAssured.baseURI + endPoint);
        return response;
    }

    public static RequestSpecification setParams(Map<String, Object> params){
        RequestSpecification requestSpec = RestAssured.given();
        for(Map.Entry<String,Object> entry: params.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.startsWith("path_")){
                requestSpec.pathParam(key.replace("path_",""), value);
            }else if(key.startsWith("query_")){
                requestSpec.queryParam(key.replace("query_",""), value);
            }else if(key.startsWith("header_")){
                requestSpec.header(key.replace("header_",""), value);
            }else if(key.startsWith("multipart_")){
                requestSpec.multiPart(key.replace("multipart_",""), value);
            }
        }
        return requestSpec;
    }

    public static String getAccessToken(String credentialsFile){
        try{
            FileInputStream fis = new FileInputStream(credentialsFile);
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(fis)
                    .createScoped("https://www.googleapis.com/auth/drive");
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        }catch (IOException e){
            LogUtil.logError(ApiUtil.class, "Error while generating access token. " + e);
            throw new RuntimeException("Error while generating access token. ", e);
        }
    }


}
