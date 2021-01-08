package com.hims.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WardRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Integer> findByTreatmentAreaId(int t_area_id) {
        String sql = "select id from ward where t_area_id=?";
        try {
            return jdbcTemplate.queryForList(sql, Integer.class, t_area_id);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer findTAreaIdByWardId(int id){
        String sql = "select t_area_id from ward where id=?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, id);
        } catch (Exception e) {
            return null;
        }
    }
}
