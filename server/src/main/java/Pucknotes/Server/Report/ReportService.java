package Pucknotes.Server.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The ReportService class provides methods for creating, retrieving, and deleting reports.
 * It acts as a service layer to handle business logic related to reports without dealing
 * directly with the data access layer.
 */
@Service
public class ReportService {

    @Autowired
    private ReportRepository repository; // The repository for managing Report entities.

    /**
     * Creates a new report and saves it to the database.
     * 
     * @param report The report object to be created.
     * @return The saved report object, which may include additional values populated by the database.
     * @throws IllegalArgumentException if the report is null or invalid.
     */
    public Report createReport(Report report) {
        // Check if the report object is null before proceeding.
        if (report == null) {
            throw new IllegalArgumentException("Report must not be null.");
        }
        return repository.save(report); // Save the report to the database and return the saved instance.
    }

    /**
     * Retrieves all reports from the database.
     * 
     * @return A list of all reports in the database.
     * This may return an empty list if there are no reports.
     */
    public List<Report> getAllReports() {
        // Fetch all reports from the repository.
        return repository.findAll(); // Return the list of reports, which can be empty but never null.
    }

    /**
     * Deletes the report with the specified ID from the database.
     * 
     * @param id The unique identifier of the report to be deleted.
     * @throws IllegalArgumentException if the id is null or empty.
     * @throws ReportNotFoundException if no report is found with the given id (this can be thrown by the repository).
     */
    public void deleteReport(String id) {
        // Validate the id before attempting to delete.
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID must not be null or empty.");
        }
        repository.deleteById(id); // Delete the report by its ID from the database.
    }
}
