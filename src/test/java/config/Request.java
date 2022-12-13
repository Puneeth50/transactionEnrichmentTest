package config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.OAuthTokenRequest;
import org.hamcrest.Matchers;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class Request {

    private String host = "https://api.richest.com";

    public void verifyResponseStatusValue(Response response, int expectedCode) {
        assertThat(response.getStatusCode(), Matchers.is(expectedCode));
    }

    public Response requestWithInvalidFieldNames(String url, String payLoad) {
        RequestSpecification request = RestAssured.given().baseUri(host);
        request.header("Content-Type", "application/json");
        return request.body(payLoad).post(url);
    }

    public void verfiyErrorMessage(Response response, String expectedError) {
        String actualError = response.body().jsonPath().get("error");
        assertThat(actualError, Matchers.is(expectedError));
    }

    protected String createJSONPayload(Object pojo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(pojo);
    }

    public Response processHttpRequest(String requestType, String url, Object pojo) throws JsonProcessingException {
        RequestSpecification request = RestAssured.given().baseUri(host);
        Response response = null;

        if (null != pojo) {
            String payload = createJSONPayload(pojo);
            request.body(payload);
        }
        switch (requestType.toUpperCase()) {
            case "GET":
                response = request.get(url);
                break;
            case "POST":
                response = request.post(url);
                break;
            default:
                throw new IllegalArgumentException("No Such method " + requestType.toUpperCase());
        }
        return response;
    }

    public Response processHttpRequestWithAuth(String requestType, String url, Object pojo, String authType) throws JsonProcessingException {
        RequestSpecification request = RestAssured.given().baseUri(host);
        if(authType.equalsIgnoreCase("unauth")){
            request.header("Authorization", "Bearer unauthorised");
        } else if (authType.equalsIgnoreCase("expired")) {
            request.header("Authorization", "Bearer expired");
        }else{
            request.header("Authorization", "Bearer " + getAccessToken());
        }

        Response response = null;

        if (null != pojo) {
            String payload = createJSONPayload(pojo);
            request.body(payload);
        }
        switch (requestType.toUpperCase()) {
            case "GET":
                response = request.get(url);
                break;
            case "POST":
                response = request.post(url);
                break;
            default:
                throw new IllegalArgumentException("No Such method " + requestType.toUpperCase());
        }
        return response;
    }

    private String getAccessToken() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object obj = null;
        try {
            obj = objectMapper.readValue(new ClassPathResource("/payloads/OAuthTokenRequest.json").getFile(), OAuthTokenRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return processHttpRequest("POST", "/v1/oauth/token", obj).jsonPath().get("access_token");
    }

}