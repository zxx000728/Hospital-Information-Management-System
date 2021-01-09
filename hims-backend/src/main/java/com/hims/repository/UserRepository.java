package com.hims.repository;

import com.hims.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(User user) {
        String sql = "insert into user(id,name,password,age,email,phone,u_type) values(?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, user.getId(), user.getName(), user.getPassword(), user.getAge(), user.getEmail(), user.getPhone(), user.getU_type());
    }

    public void delete(int id) {
        String sql = "delete from user where id=?";
        jdbcTemplate.update(sql, id);
    }

    public void update(int id, String name, String password, String age, String email, String phone) {
        String sql = "update user set name=?,password=?,age=?,email=?,phone=? where id=?";
        jdbcTemplate.update(sql, name, password, age, email, phone, id);
    }

    public User find(int id) {
        String sql = "select * from user where id=?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
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

    public List<User> findByType(String type) {
        String sql = "select * from user where u_type=?";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), type);
        } catch (Exception e) {
            return null;
        }
    }

}
