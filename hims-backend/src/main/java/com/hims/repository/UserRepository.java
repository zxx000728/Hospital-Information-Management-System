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
    JdbcTemplate jdbcTemplate;

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
