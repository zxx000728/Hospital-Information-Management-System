package com.hims.serviceImpl;

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

    public void addNATReport(int p_id) {
        natReportRepository.insert(p_id);
    }

    public List<NatReport> getNATReport(int p_id) {
        return natReportRepository.findByPId(p_id);
    }
}
