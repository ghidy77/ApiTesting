package com.cipa.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class RestUtils {

    private static final Logger LOG = LogManager.getLogger(RestUtils.class);
    private static final String REQUEST_PATH = "src/test/resources/requests/";
    protected static RequestSpecification request;
    protected static String baseUrl = "";
    protected static String env = PropertyReader.getProperty("env");
    protected static String auth = PropertyReader.getProperty("auth");

    public static void setup(){
        if (env.toLowerCase().contentEquals("dev")){
            baseUrl = PropertyReader.getProperty("DEV_URL");
        } else if (env.toLowerCase().contentEquals("test")) {
            baseUrl = PropertyReader.getProperty("TEST_URL");
        } else {
            LOG.warn("Invalid Enviroment");
            throw new IllegalStateException("Invalid Environment");
        }

        request = given().log().all().header("Authorization", "Basic " + auth);
    }

    public static String getRequestBody(String filename){
        String requestBody = "";
        String path = REQUEST_PATH + filename;
        try{
            requestBody = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e){
            LOG.error("File not found at path:" + path);
        }
        return requestBody;
    }

    public static Response getResponse(String requestBody, int expectedStatusCode){
        return request.when().body(requestBody).get(baseUrl).then().log().body().assertThat().statusCode(expectedStatusCode).contentType(ContentType.JSON).extract().response();
    }

    public static JsonObject getRequestBodyAsJson(String filename){
        Reader reader = null;
        try {
            // open file
            reader = new InputStreamReader(new FileInputStream(REQUEST_PATH + filename));
        } catch (FileNotFoundException e1) {
            LOG.warn("File not found.", e1);
        }
        // json from the json file located in src/main/resources
        JsonElement jsonElemFromFile = JsonParser.parseReader(new JsonReader(reader));
        if (jsonElemFromFile.isJsonObject()){
            return jsonElemFromFile.getAsJsonObject();
        } else {
            LOG.warn("Invalid JSON File");
            return null;
        }
    }

    public static void printResponse(HttpResponse response) throws IOException {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        System.out.println("Status code is: " + response.getStatusLine().getStatusCode());
        System.out.println("Request message is: " + response.getStatusLine().getReasonPhrase());

        response.getEntity().writeTo(outstream);
        System.out.println("Response Body: \n " + outstream.toString());
    }


}
