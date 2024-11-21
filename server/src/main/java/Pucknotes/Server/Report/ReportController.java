package Pucknotes.Server.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reports;

    @PostMapping("/create")
    public Report createReport(@RequestBody Report report) {
        return reports.createReport(report);
    }

    @GetMapping
    public List<Report> getAllReports() {
        return reports.getAllReports();
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable String id) {
        reports.deleteReport(id);
    }
}
