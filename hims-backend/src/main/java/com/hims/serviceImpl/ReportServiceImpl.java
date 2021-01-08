package com.hims.serviceImpl;

import com.hims.repository.DailyReportRepository;
import com.hims.repository.NATReportRepository;
import com.hims.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
