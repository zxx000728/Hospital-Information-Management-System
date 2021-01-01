package com.hims.repository;

import com.hims.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.List;

@Repository
@PropertySource({"classpath:application.properties"})
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value(value = "${spring.datasource.driver-class-name}")
    private String driver;
    @Value(value = "${spring.datasource.initial_url}")
    private String initial_url;
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
    @Value(value = "${spring.datasource.insertUser}")
    private String insertUser;

    @PostConstruct
    public void init() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(initial_url, userName, password);
        Statement stat = conn.createStatement();
        stat.executeUpdate(createDatabase);
        stat.execute(useDatabase);
        stat.executeUpdate(createUser);
        stat.executeUpdate(insertUser);
        stat.close();
        conn.close();
    }

    public void save(User user) {
        String sql = "insert into user(id,name,password,age,email,phone,u_type) values(?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, user.getId(), user.getName(), user.getPassword(), user.getAge(), user.getEmail(), user.getPhone(), user.getU_type());
    }

    public void delete(String id) {
        String sql = "delete from user where id=?";
        jdbcTemplate.update(sql, id);
    }

    public User find(String id) {
        String sql = "select * from user where id=?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), Integer.parseInt(id));
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> findAll() {
        String sql = "select * from user";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            return null;
        }
    }
}
