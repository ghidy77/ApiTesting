package com.cipa.entity;

import com.cipa.utils.RestUtils;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Vendor {

    private String vendorId;

    public Vendor(String name){
        addVendor(name);
    }

    public Response addVendor(String name){
        String addClientTemplate = RestUtils.getRequestBody("addVendor.json");
        String requestBody = MessageFormat.format(addClientTemplate, name);

        return RestUtils.getResponse(requestBody, HttpStatus.SC_OK);
    }

    public Response deleteVendor(){
        String deleteClientTemplate = RestUtils.getRequestBody("deleteVendor.json");
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String requestBody = MessageFormat.format(deleteClientTemplate, endDate);

        return RestUtils.getResponse(requestBody, HttpStatus.SC_OK);
    }

    public String getVendorId(){
        return vendorId;
    }
}
