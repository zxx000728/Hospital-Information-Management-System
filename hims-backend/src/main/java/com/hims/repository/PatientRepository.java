package com.hims.repository;

import com.hims.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PatientRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Patient> findByWardId(int w_id) {
        String sql = "select patient.* from patient,bed where bed.w_id=? and patient.bed_id=bed.id";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class), w_id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Patient> findByWNurseId(int w_nurse_id) {
        String sql = "select * from patient where w_nurse_id=?";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class), w_nurse_id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Patient> findHospitalizedByWNurseId(int w_nurse_id) {
        String sql = "select * from patient where w_nurse_id=? and state='hospitalized'";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class), w_nurse_id);
        } catch (Exception e) {
            return null;
        }
    }
}
