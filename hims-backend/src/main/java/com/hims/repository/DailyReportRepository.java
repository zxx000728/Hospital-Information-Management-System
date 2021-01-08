package com.hims.repository;

import com.hims.domain.DailyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DailyReportRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(DailyReport dailyReport) {
        String sql = "insert into daily_report(p_id,date,temperature,symptom,state,w_nurse_id,n_report_id) values (?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, dailyReport.getP_id(), dailyReport.getDate(), dailyReport.getTemperature(),
                dailyReport.getSymptom(), dailyReport.getState(), dailyReport.getW_nurse_id(), dailyReport.getN_report_id());
    }
}
