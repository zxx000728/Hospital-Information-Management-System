package com.hims.serviceImpl;

import com.hims.domain.DailyReport;
import com.hims.domain.NatReport;
import com.hims.repository.DailyReportRepository;
import com.hims.repository.NATReportRepository;
import com.hims.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private NATReportRepository natReportRepository;
    @Autowired
    private DailyReportRepository dailyReportRepository;

    @Autowired
    public ReportServiceImpl(NATReportRepository natReportRepository, DailyReportRepository dailyReportRepository) {
        this.natReportRepository = natReportRepository;
        this.dailyReportRepository = dailyReportRepository;
    }

    public String addNATReport(int p_id) {
        if (natReportRepository.isNATUnfilled(p_id)) {
            natReportRepository.insert(p_id);
            return "OK!";
        } else {
            return "Blank NAT report exists!";
        }
    }

    public String addDailyReport(int p_id, String date, float temperature,
                                 String symptom, String state, int w_nurse_id) {
        if (dailyReportRepository.isDailyUnfilled(p_id, date)) {
            dailyReportRepository.insert(p_id, date, temperature, symptom, state, w_nurse_id);
            return "OK!";
        } else {
            return "Daily report exists!";
        }
    }

    public List<NatReport> getNATReport(int p_id) {
        return natReportRepository.findByPId(p_id);
    }

    public List<DailyReport> getDailyReport(int p_id) {
        return dailyReportRepository.findByPId(p_id);
    }

    public void fillNATReport(String id, String result, String date, String time) {
        natReportRepository.update(Integer.parseInt(id), result, date, time);
    }
}
