package Pucknotes.Server.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import Pucknotes.Server.Response.Types.UnauthorizedException;
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
        int accountRole = sessions.getCurrentUser(request).getRole();
        if(accountRole == 0 || accountRole == 1){
            throw new UnauthorizedException("User deos not have the correct permissions to view all reports");
        }
        return reports.getAllReports();
    }

    @DeleteMapping("/{id}")
    public void deleteReport(HttpServletRequest request, @PathVariable String id) {
        int accountRole = sessions.getCurrentUser(request).getRole();
        if(accountRole == 0 || accountRole == 1){
            throw new UnauthorizedException("User deos not have the correct permissions to delete a report");
        }
        reports.deleteReport(id);
    }
}
