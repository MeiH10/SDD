package Pucknotes.Server.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Response.APIResponse;
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
    public ResponseEntity<APIResponse<String>> createReport(
            HttpServletRequest request,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") String type,
            @RequestParam("item") String itemId) {
        
        Account user = sessions.getCurrentUser(request);
        
        Report report = new Report();
        report.setTitle(title);
        report.setDescription(description);
        report.setType(type);
        report.setItem(itemId);
        report.setOwner(user.getId());
        
        report = reports.createReport(report);
        
        return ResponseEntity.ok(APIResponse.good(report.getId()));
    }

    @GetMapping("")
    public ResponseEntity<APIResponse<List<Report>>> getAllReports(HttpServletRequest request) {
        Account user = sessions.getCurrentUser(request);
        return ResponseEntity.ok(APIResponse.good(reports.getAllReports()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteReport(
            HttpServletRequest request,
            @PathVariable String id) {
        Account user = sessions.getCurrentUser(request);

        reports.deleteReport(id);
        return ResponseEntity.ok(APIResponse.good(true));
    }
}