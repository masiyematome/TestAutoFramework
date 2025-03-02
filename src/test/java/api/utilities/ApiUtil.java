package api.utilities;

import api.constants.HttpMethod;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import java.util.Map;


public class ApiUtil {

    public static Response sendRequest(HttpMethod method, String endPoint, String requestBody, Map<String, String> params){
        var request = setParams(params);
        return switch (method){
            case GET -> request
                    .when()
                    .get(endPoint)
                    .then().statusCode(HttpStatus.SC_OK).extract().response();
            case POST -> request
                    .body(requestBody)
                    .when()
                    .post(endPoint)
                    .then().statusCode(HttpStatus.SC_CREATED).extract().response();
            case PUT -> request
                    .when()
                    .put(endPoint)
                    .then().extract().response();
            case DELETE -> request
                    .when()
                    .delete(endPoint)
                    .then().extract().response();
            default -> throw new UnsupportedOperationException("Method '" + method + "' unsupported.");
        };
    }

    public static RequestSpecification setParams(Map<String, String> params){
        RequestSpecification requestSpec = RestAssured.given();
        for(Map.Entry<String,String> entry: params.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            if(key.startsWith("path_")){
                requestSpec.pathParam(key.replace("path_",""), value);
            }else if(key.startsWith("query_")){
                requestSpec.queryParam(key.replace("query_",""), value);
            }else if(key.startsWith("header_")){
                requestSpec.header(key.replace("header_",""), value);
            }
        }
        return requestSpec;
    }
}
