package com.ilnur.jdbc;

import com.ilnur.jdbc.util.ConnectionManager;

import java.sql.SQLException;
import java.sql.Statement;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        try (var connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
            System.out.println(connection.getTransactionIsolation());
        }
    }
}
