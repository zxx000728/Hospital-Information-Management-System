package com.hims.repository;

import com.hims.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WardNurseAndWardRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> findWardNurseByWardId(int w_id) {
        String sql = "select user.* from user,ward_nurse_ward where ward_nurse_ward.w_id=? and ward_nurse_ward.w_nurse_id=user.id";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), w_id);
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteWardNurseByWNurseId(int w_nurse_id) {
        String sql = "delete from ward_nurse_ward where w_nurse_id=?";
        jdbcTemplate.update(sql, w_nurse_id);
    }

    public Integer findWardIdByWNurseId(int w_nurse_id){
        String sql = "select w_id from ward_nurse_ward where w_nurse_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, w_nurse_id);
        } catch (Exception e) {
            return null;
        }
    }

//    public List<User> findFreeWardNurse() {
//        String sql = "select distinct user.* from user,ward_nurse_ward where user.u_type='w_nurse' and ward_nurse_ward.w_nurse_id != user.id";
//        try {
//            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public void insertWardNurse(int w_nurse_id, int w_id) {
//        String sql = "insert into ward_nurse_ward(w_nurse_id,w_id) values (?,?)";
//        jdbcTemplate.update(sql, w_nurse_id, w_id);
//    }
}
