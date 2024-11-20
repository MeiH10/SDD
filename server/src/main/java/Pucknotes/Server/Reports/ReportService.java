package com.example.reportsapi.service;

import com.example.reportsapi.model.Report;
import com.example.reportsapi.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
