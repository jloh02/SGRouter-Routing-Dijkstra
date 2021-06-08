package com.jonathan.sgrouter.routing.utils;

import java.sql.Connection;
import java.sql.SQLException;

import com.jonathan.sgrouter.routing.RoutingApplication;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {
    private static HikariDataSource ds;
    private static HikariConfig config;

    public static Connection getConnection() throws SQLException {      
        config = new HikariConfig(); 
        final String filename = RoutingApplication.config.isAppengineDeployment() ? "/tmp/graph.db" : "graph.db";
        config.setJdbcUrl("jdbc:sqlite:" + filename);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        ds = new HikariDataSource(config);
        return ds.getConnection();
    }

    public static void close(){
        ds.close();
    }
}
