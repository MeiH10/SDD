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
        int accountRole = account.getRole();
        if(accountRole == 0 || accountRole == 1){
            throw new UnauthorizedException("User deos not have the correct permissions to view all reports");
        }
        return repository.findAll();

    }

    public void deleteReport(String id) {
        int accountRole = account.getRole();
        if(accountRole == 0 || accountRole == 1){
            throw new UnauthorizedException("User deos not have the correct permissions to delete a report");
        }

        repository.deleteById(id);
    }
}
