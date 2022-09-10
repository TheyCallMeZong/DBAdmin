package com.database.dbadmin.database;

import java.sql.*;

public class PostgresSqlConnect {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "1012";
    final Connection connection;

    {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static PostgresSqlConnect instance;

    public static PostgresSqlConnect getInstance() {
        return instance == null ? new PostgresSqlConnect() : instance;
    }
}
