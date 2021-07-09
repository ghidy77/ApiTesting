package com.cipa;

import com.cipa.base.TestBase;
import com.cipa.entity.Client;
import com.cipa.utils.DbConn;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

public class ClientTest extends TestBase {

    private static Client client;

    @Test
    public static void addClientTest(){
        client = new Client();
        Assert.assertFalse(client.getClientId().isEmpty(), "Client id is not empty");
        test.pass("Client id was generated correctly");
    }

    @Test
    public static void validateClientEntry() throws SQLException, IOException {
        String query = DbConn.getQuery("getClients");
        ResultSet rs = DbConn.execute(query, Arrays.asList("1"));
        Map<String, String> results = DbConn.getMapOfColumnsAndValues(rs);
        Assert.assertFalse(results.isEmpty(), "No results were found");
        test.pass("Query returned a list of results");
        Assert.assertTrue(results.containsValue("Generic Client"), "No results were found");
        test.pass("Entry from DB contains expected client name");
        Assert.assertEquals(results.get("Country"), "RO", "Incorrect country for Client");
        test.pass("Entry from DB contains correct country");
    }

    @AfterMethod
    public static void cleanup(){
        if (client != null){
            client.deleteClient();
        }
    }
}
