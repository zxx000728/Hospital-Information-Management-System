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

    public Integer findWardIdByWNurseId(int w_nurse_id) {
        String sql = "select w_id from ward_nurse_ward where w_nurse_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, w_nurse_id);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer findFreeWardNurse(int w_id, int t_area_id) {
        String sql = "select w_nurse_id from ward_nurse_ward where w_id=?";
        try {
            List<Integer> w_nurse_id = jdbcTemplate.queryForList(sql, Integer.class, w_id);
            sql = "select count(id) from patient where w_nurse_id=? and state='hospitalized'";
            for (Integer integer : w_nurse_id) {
                System.out.println("w_nurse_id" + w_nurse_id);
                int patientCount = jdbcTemplate.queryForObject(sql, Integer.class, integer);
                System.out.println(patientCount);
                if (t_area_id == 1 && patientCount < 3) {
                    return integer;
                } else if (t_area_id == 2 && patientCount < 2) {
                    return integer;
                } else if (t_area_id == 3 && patientCount < 1) {
                    return integer;
                }
            }
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    public void insertWardNurse(int w_nurse_id, int w_id) {
        String sql = "insert into ward_nurse_ward(w_nurse_id,w_id) values (?,?)";
        jdbcTemplate.update(sql, w_nurse_id, w_id);
    }
}
