package com.hims.repository;

import com.hims.domain.Bed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BedRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateState(int id, String state) {
        String sql = "UPDATE bed SET state=? WHERE id=?";
        jdbcTemplate.update(sql, state, id);
    }

    public List<Bed> findOccupiedByWardId(int w_id) {
        String sql = "select bed.*,patient.id as p_id,patient.name as p_name from patient,bed where bed.w_id=? and patient.bed_id=bed.id";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Bed.class), w_id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Bed> findFreeByWardId(int w_id) {
        String sql = "select bed.* from bed where bed.w_id=? and bed.state='free'";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Bed.class), w_id);
        } catch (Exception e) {
            return null;
        }
    }
}
