package com.database.dbadmin.database;

import java.sql.*;

public class PostgresSqlConnect extends Config{
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
