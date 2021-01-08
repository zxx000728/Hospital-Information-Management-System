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

    public List<Patient> findAll() {
        String sql = "select patient.*,bed.w_id,ward.t_area_id from patient,bed,ward where patient.bed_id=bed.id and bed.w_id=ward.id";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class));
        } catch (Exception e) {
            return null;
        }
    }

    public List<Patient> findByWardId(int w_id) {
        String sql = "select patient.*,bed.w_id,ward.t_area_id from patient,bed,ward where bed.w_id=? and patient.bed_id=bed.id and bed.w_id=ward.id";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class), w_id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Patient> findByWNurseId(int w_nurse_id) {
        String sql = "select patient.*,bed.w_id,ward.t_area_id from patient,bed,ward where patient.w_nurse_id=? and patient.bed_id=bed.id and bed.w_id=ward.id";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class), w_nurse_id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Patient> findHospitalizedByWNurseId(int w_nurse_id) {
        String sql = "select patient.*,bed.w_id,ward.t_area_id from patient,bed,ward where patient.w_nurse_id=? and patient.state='hospitalized'and patient.bed_id=bed.id and bed.w_id=ward.id";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class), w_nurse_id);
        } catch (Exception e) {
            return null;
        }
    }
}
