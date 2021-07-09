package com.cipa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbConn {

    private static final String DBUSER = PropertyReader.getProperty("dbuser");
    private static final String DBPASS = PropertyReader.getProperty("dbpass");
    private static final String DBURL = PropertyReader.getProperty("dburl");
    private static Connection conn;

    private static final Logger LOG = LogManager.getLogger(DbConn.class);

    public static Connection getInstance() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                LOG.info("Connected successfully.");

            } catch (SQLException e) {
                e.printStackTrace();
                LOG.error("There was an error with the query: " + e.getMessage());
            }
        }
        return conn;
    }

    public static void close() throws SQLException {
        if (conn == null || conn.isClosed()) {
            return;
        } else {
            conn.close();
        }
    }

    public static ResultSet executeQueryFromFile(String filename) throws IOException {
        return DbConn.execute(DbConn.getQuery(filename));
    }

    public static String getQuery(String sqlFilename) throws IOException {
        String sqlFilePath = "src/test/resources/queries/" + sqlFilename + ".sql";
        Path path = Paths.get(sqlFilePath);
        return Files.readAllLines(path).stream().filter(str -> !str.contains("--")).collect(Collectors.joining(" "));
    }

    private static PreparedStatement createPreparedStatement(String query, List<String> params) throws SQLException {
        PreparedStatement preparedStatement = getInstance().prepareStatement(query);
        if (params.size() > 0) {
            for (int i = 1; i <= params.size(); i++) {
                preparedStatement.setString(i, params.get(i - 1));
            }
        }
        return preparedStatement;
    }

    public static ResultSet execute(String query) {
        return execute(query, new ArrayList<>());
    }

    public static ResultSet execute(String query, List<String> params) {
        ResultSet result = null;
        try {
            result = createPreparedStatement(query, params).executeQuery();
        } catch (SQLException e) {
            LOG.error("SQL Exception: " + e.getMessage());
        }

        return result;
    }

    /**
     * This method returns a list of values for a column
     *
     * @param result
     * @param column
     * @return
     * @throws SQLException
     */
    public static List<String> getColumnValues(ResultSet result, String column) throws SQLException {
        List<String> results = new ArrayList<>();
        while (result.next()) {
            results.add(result.getString(column));
        }
        return results;
    }

    public static Map<String, String> getMapOfColumnsAndValues(ResultSet rs) throws SQLException {
        Map<String, String> results = new HashMap<>();
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();

        // fetch only the first row
        rs.next();

        // getColumnName starts from 1
        for (int i = 1; i <= columns; i++) {
            results.put(md.getColumnName(i), rs.getString(i));
        }
        return results;
    }
}