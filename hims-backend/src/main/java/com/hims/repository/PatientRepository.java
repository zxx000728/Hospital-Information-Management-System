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
}
