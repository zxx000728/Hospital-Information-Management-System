package com.hims.repository;

import com.hims.domain.DailyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DailyReportRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    public void insert(DailyReport dailyReport) {
//        String sql = "insert into daily_report(p_id,date,temperature,symptom,state,w_nurse_id,n_report_id) values (?,?,?,?,?,?,?)";
//        jdbcTemplate.update(sql, dailyReport.getP_id(), dailyReport.getDate(), dailyReport.getTemperature(),
//                dailyReport.getSymptom(), dailyReport.getState(), dailyReport.getW_nurse_id(), dailyReport.getN_report_id());
//    }

    public boolean isDailyUnfilled(int p_id, String date) {
        List<DailyReport> dailyReports = findByPId(p_id);
        if (dailyReports == null || dailyReports.size() == 0) {
            return true;
        }
        System.out.println(dailyReports.size());
        for (DailyReport dailyReport : dailyReports) {
            System.out.println(dailyReport.getDate());
            if (dailyReport.getDate().equals(date)) {
                return false;
            }
        }
        return true;
    }

    public List<DailyReport> findByPId(int p_id) {
        String sql = "select * from daily_report where p_id = ?";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DailyReport.class), p_id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<DailyReport> findByPIdAndDate(int p_id) {
        String sql = "select * from daily_report where p_id = ? ORDER BY date DESC";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DailyReport.class), p_id);
        } catch (Exception e) {
            return null;
        }
    }

    public void insert(int p_id, String date, float temperature,
                       String symptom, String state, int w_nurse_id) {
        String sql = "insert into daily_report(p_id,date,temperature,symptom,state,w_nurse_id) values (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, p_id, date, temperature, symptom, state, w_nurse_id);
    }
}
