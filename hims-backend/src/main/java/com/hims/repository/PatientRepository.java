package com.hims.repository;

import com.hims.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PatientRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    public Patient find(int id) {
//        String sql = "select * from patient where id=?";
//        return jdbcTemplate.queryForObject(sql, Patient.class, id);
//    }

    public void update(int id, int w_nurse_id, int bed_id, String state,
                       int is_to_be_released, int is_to_be_transferred) {
        String sql = "UPDATE patient SET w_nurse_id=?,bed_id=?,state=?,is_to_be_released=?,is_to_be_transferred=? WHERE id=?";
        jdbcTemplate.update(sql, w_nurse_id, bed_id, state, is_to_be_released, is_to_be_transferred, id);
    }

    public void updateTArea(int id, int is_to_be_released, int is_to_be_transferred) {
        String sql = "UPDATE patient SET is_to_be_released=?,is_to_be_transferred=? WHERE id=?";
        jdbcTemplate.update(sql, is_to_be_released, is_to_be_transferred, id);
    }

    public String getPatientState(int id) {
        String sql = "select state from patient where id=?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
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

    public Patient find(int id) {
        List<Patient> patients = findAll();
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    public List<Patient> findAll() {
        String sql = "select patient.*,bed.w_id,ward.t_area_id from patient left join bed on patient.bed_id=bed.id left join ward on bed.w_id=ward.id";
        try {
//            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class));
            List<Patient> patients = jdbcTemplate.query(sql, rs -> {
                List<Patient> list = new ArrayList<>();
                while (rs.next()) {
                    Patient patient = new Patient();
                    patient.setId(rs.getInt("id"));
                    patient.setName(rs.getString("name"));
                    patient.setAge(rs.getString("age"));
                    patient.setPhone(rs.getString("phone"));
                    patient.setAddress(rs.getString("address"));
                    patient.setRating(rs.getString("rating"));
                    patient.setE_nurse_id(rs.getInt("e_nurse_id"));
                    patient.setW_nurse_id(rs.getInt("w_nurse_id"));
                    patient.setT_area_id(rs.getInt("t_area_id"));
                    patient.setW_id(rs.getInt("w_id"));
                    patient.setBed_id(rs.getInt("bed_id"));
                    patient.setState(rs.getString("state"));
                    patient.setIs_to_be_released(rs.getBoolean("is_to_be_released"));
                    patient.setIs_to_be_transferred(rs.getBoolean("is_to_be_transferred"));
                    list.add(patient);
                }
                return list;
            });
            return patients;
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
