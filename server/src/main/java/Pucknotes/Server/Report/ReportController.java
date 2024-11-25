package Pucknotes.Server.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Session.SessionService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reports;

    @Autowired
    private SessionService sessions;

    @PostMapping("")
    public Report createReport(@RequestBody Report report) {
        return reports.createReport(report);
    }

    @GetMapping("")
    public List<Report> getAllReports(HttpServletRequest request) {
        Account account = sessions.getCurrentUser(request);
        return reports.getAllReports(account);
    }

    @DeleteMapping("/{id}")
    public void deleteReport(HttpServletRequest request, @PathVariable String id) {
        Account account = sessions.getCurrentUser(request);
        reports.deleteReport(id, account);
    }
}
