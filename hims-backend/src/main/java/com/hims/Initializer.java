package com.hims;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
class Initializer {
    @Value(value = "${spring.datasource.driver-class-name}")
    private String driver;
    @Value(value = "${spring.datasource.initial_url}")
    private String initial_url;
    @Value(value = "${spring.datasource.username}")
    private String userName;
    @Value(value = "${spring.datasource.password}")
    private String password;
    @Value(value = "${spring.datasource.createDatabase}")
    private String createDatabase;
    @Value(value = "${spring.datasource.useDatabase}")
    private String useDatabase;
    @Value(value = "${spring.datasource.createUser}")
    private String createUser;
    @Value(value = "${spring.datasource.insertUser}")
    private String insertUser;
    @Value(value = "${spring.datasource.createTreatment_area}")
    private String createTreatment_area;
    @Value(value = "${spring.datasource.insertTreatment_area}")
    private String insertTreatment_area;
    @Value(value = "${spring.datasource.createWard}")
    private String createWard;
    @Value(value = "${spring.datasource.insertWard}")
    private String insertWard;
    @Value(value = "${spring.datasource.createWard_nurse_ward}")
    private String createWard_nurse_ward;
    @Value(value = "${spring.datasource.insertWard_nurse_ward}")
    private String insertWard_nurse_ward;
    @Value(value = "${spring.datasource.createBed}")
    private String createBed;
    @Value(value = "${spring.datasource.insertBed}")
    private String insertBed;
    @Value(value = "${spring.datasource.createPatient}")
    private String createPatient;
    @Value(value = "${spring.datasource.createNAT_report}")
    private String createNAT_report;
    @Value(value = "${spring.datasource.createDaily_report}")
    private String createDaily_report;

    @Bean
    public void init() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(initial_url, userName, password);
        Statement stat = conn.createStatement();
        stat.executeUpdate(createDatabase);
        stat.execute(useDatabase);
        stat.executeUpdate(createUser);
        stat.executeUpdate(insertUser);
        stat.executeUpdate(createTreatment_area);
        stat.executeUpdate(insertTreatment_area);
        stat.executeUpdate(createWard);
        stat.executeUpdate(insertWard);
        stat.executeUpdate(createWard_nurse_ward);
        stat.executeUpdate(insertWard_nurse_ward);
        stat.executeUpdate(createBed);
        stat.executeUpdate(insertBed);
        stat.executeUpdate(createPatient);
        stat.executeUpdate(createNAT_report);
        stat.executeUpdate(createDaily_report);
        stat.close();
        conn.close();
    }

}
