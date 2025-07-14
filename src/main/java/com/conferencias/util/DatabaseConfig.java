package com.conferencias.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:sqlserver://localhost:1433;databaseName=ConferenciasDB;encrypt=true;trustServerCertificate=true");
            config.setUsername("sa");  // Reemplaza con tu usuario de SQL Server
            config.setPassword("12072003"); // Reemplaza con tu contraseña
            config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Configuración del pool de conexiones
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setIdleTimeout(30000);
            config.setPoolName("ConferenciasHikariCP");

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            System.err.println("Error al inicializar el pool de conexiones: " + e.getMessage());
            throw new RuntimeException("Error al inicializar la conexión a la base de datos", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}