package Pucknotes.Server.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository repository;

    public Report createReport(Report report) {
        return repository.save(report);
    }

    public List<Report> getAllReports() {
        return repository.findAll();
    }

    public void deleteReport(String id) {
        repository.deleteById(id);
    }
}
