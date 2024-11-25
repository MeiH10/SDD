package Pucknotes.Server.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Session.SessionService;
import Pucknotes.Server.Response.APIResponse;
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
    public ResponseEntity<APIResponse<Report>> createReport(
            HttpServletRequest request,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") String type,
            @RequestParam("item") String item) {
        
        Account user = sessions.getCurrentUser(request);
        Report report = new Report(null, user.getId(), item, type, title, description);
        report = reports.createReport(report);
        return ResponseEntity.ok(APIResponse.good(report));
    }

    @GetMapping("")
    public ResponseEntity<APIResponse<List<Report>>> getAllReports(HttpServletRequest request) {
        Account account = sessions.getCurrentUser(request);
        List<Report> reportList = reports.getAllReports(account);
        return ResponseEntity.ok(APIResponse.good(reportList));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteReport(
            HttpServletRequest request, 
            @PathVariable String id) {
        Account account = sessions.getCurrentUser(request);
        reports.deleteReport(id, account);
        return ResponseEntity.ok(APIResponse.good(true));
    }
}