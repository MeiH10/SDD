package Pucknotes.Server.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Response.Types.UnauthorizedException;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository repository;

    public Report createReport(Report report) {
        return repository.save(report);
    }

    public List<Report> getAllReports(Account account) {
        int role = account.getRole();
        if (role == 0 || role == 1) {
            throw new UnauthorizedException("User deos not have the correct permissions to view all reports");
        }
        return repository.findAll();
    }

    public void deleteReport(String id, Account account) {
        int role = account.getRole();
        if (role == 0 || role == 1) {
            throw new UnauthorizedException("User deos not have the correct permissions to view all reports");
        }
        repository.deleteById(id);
    }
}
