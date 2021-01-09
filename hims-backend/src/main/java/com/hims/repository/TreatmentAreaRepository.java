package com.hims.repository;

import com.hims.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TreatmentAreaRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findHeadNurseByDoctorId(int doctor_id) {
        String sql = "select user.* from user,treatment_area where treatment_area.doctor_id=? and treatment_area.h_nurse_id=user.id";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), doctor_id);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer findByDoctorId(int doctor_id) {
        String sql = "select id from treatment_area where doctor_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, doctor_id);
        } catch (Exception e) {
            return null;
        }
    }

    public String findTypeByHNurseId(int h_nurse_id) {
        String sql = "select type from treatment_area where h_nurse_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, h_nurse_id);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer findByHeadNurseId(int h_nurse_id) {
        String sql = "select id from treatment_area where h_nurse_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, h_nurse_id);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer findByType(String type) {
        String sql = "select id from treatment_area where type=?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, type);
        } catch (Exception e) {
            return null;
        }
    }
}
