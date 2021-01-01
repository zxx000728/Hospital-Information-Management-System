package com.hims.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;

@Repository
@PropertySource({"classpath:application.properties"})
public class UserRepository {
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
    @Value(value = "${spring.datasource.createUser}")
    private String createUser;

    @PostConstruct
    public void init() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();
        stat.executeUpdate(createDatabase);
        stat.execute(useDatabase);
        stat.executeUpdate(createUser);
        stat.close();
        conn.close();
    }
}
