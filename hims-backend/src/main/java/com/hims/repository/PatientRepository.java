package com.hims.repository;

import com.hims.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class PatientRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void update(int id, int w_nurse_id, int bed_id, String state,
                       int is_to_be_released, int is_to_be_transferred) {
        String sql = "UPDATE patient SET w_nurse_id=?,bed_id=?,state=?,is_to_be_released=?,is_to_be_transferred=? WHERE id=?";
        jdbcTemplate.update(sql, w_nurse_id, bed_id, state, is_to_be_released, is_to_be_transferred, id);
    }

    public void updateTArea(int id, int is_to_be_released, int is_to_be_transferred) {
        String sql = "UPDATE patient SET is_to_be_released=?,is_to_be_transferred=? WHERE id=?";
        jdbcTemplate.update(sql, is_to_be_released, is_to_be_transferred, id);
    }

    public int insert(Patient patient) {
        String sql = "insert into patient(name,age,phone,address,rating,e_nurse_id) values (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, patient.getName());
            ps.setString(2, patient.getAge());
            ps.setString(3, patient.getPhone());
            ps.setString(4, patient.getAddress());
            ps.setString(5, patient.getRating());
            ps.setInt(6, patient.getE_nurse_id());
            return ps;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

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
