package com.cipa.entity;

import com.cipa.utils.RestUtils;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {
    private String clientId;

    public Client(){
        addClient();
    }

    public Response addClient(){
        JsonObject addClientTemplate = RestUtils.getRequestBodyAsJson("addClient.json");
        String contractDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        addClientTemplate.addProperty("ContractDate",contractDate);

        return RestUtils.getResponse(addClientTemplate.toString(), HttpStatus.SC_OK);
    }

    public Response deleteClient(){
        JsonObject deleteClientTemplate = RestUtils.getRequestBodyAsJson("addClient.json");
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        deleteClientTemplate.addProperty("EndDate", endDate);
        deleteClientTemplate.addProperty("Active", false);
        deleteClientTemplate.addProperty("ClientId", clientId);

        return RestUtils.getResponse(deleteClientTemplate.toString(), HttpStatus.SC_OK);
    }

    public String getClientId(){
        return clientId;
    }
}
