package com.hims.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
@PropertySource({"classpath:application.properties"})
public class NATReportRepository {
    @Value(value = "${spring.datasource.driver-class-name}")
    private String driver;
    @Value(value = "${spring.datasource.url}")
    private String url;
    @Value(value = "${spring.datasource.username}")
    private String userName;
    @Value(value = "${spring.datasource.password}")
    private String password;
    @Value(value = "${spring,datasource.createDatabase}")
    private String createDatabase;
    @Value(value = "${spring,datasource.useDatabase}")
    private String useDatabase;
    @Value(value = "${spring.datasource.createNAT_report}")
    private String createNAT_report;

    @PostConstruct
    public void init() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();
        stat.executeUpdate(createDatabase);
        stat.execute(useDatabase);
        stat.executeUpdate(createNAT_report);
        stat.close();
        conn.close();
    }
}
