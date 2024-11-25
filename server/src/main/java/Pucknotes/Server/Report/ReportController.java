package Pucknotes.Server.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Response.APIResponse;
<<<<<<< HEAD

// import Pucknotes.Server.Response.Types.UnauthorizedException;
=======
>>>>>>> origin
import Pucknotes.Server.Session.SessionService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * ReportController handles incoming HTTP requests related to reports, including creating,
 * retrieving, and deleting reports. It interacts with various services to manage the report lifecycle.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reports;

    @Autowired
    private SessionService sessions;

<<<<<<< HEAD
    @Autowired
    private NoteService notes;

    /**
     * Creates a new report based on the provided title, description, and associated note.
     *
     * @param request The HTTP request containing session information for the current user.
     * @param title The title of the report to be created.
     * @param description A detailed description of the report.
     * @param noteId The unique identifier of the note associated with the report.
     * @return A ResponseEntity containing an APIResponse with the ID of the newly created report.
     */
=======
>>>>>>> origin
    @PostMapping("")
    public ResponseEntity<APIResponse<String>> createReport(
            HttpServletRequest request,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
<<<<<<< HEAD
            @RequestParam("noteID") String noteId) {

        // Retrieve the current user from the session information.
=======
            @RequestParam("type") String type,
            @RequestParam("item") String itemId) {
        
>>>>>>> origin
        Account user = sessions.getCurrentUser(request);
        
        // Retrieve the note by its ID to associate it with the report.
        Note note = notes.getById(noteId);

        // Instantiate a new Report object to store the report data.
        Report report = new Report();
        report.setTitle(title);
        report.setDescription(description);
<<<<<<< HEAD
        report.setItem(note.getId()); // Set the associated note ID.
        report.setOwner(user.getId()); // Set the owner of the report.

        // Save the report and retrieve the saved instance.
=======
        report.setType(type);
        report.setItem(itemId);
        report.setOwner(user.getId());
        
>>>>>>> origin
        report = reports.createReport(report);

        // Return a successful response containing the new report's ID.
        return ResponseEntity.ok(APIResponse.good(report.getId()));
    }

    /**
     * Retrieves all reports from the database.
     *
     * @param request The HTTP request containing information about the user session.
     * @return A ResponseEntity containing an APIResponse with a list of all reports.
     */
    @GetMapping("")
    public ResponseEntity<APIResponse<List<Report>>> getAllReports(HttpServletRequest request) {
        // Retrieving the current user is currently commented out; user authorization may be needed here.
        
        // Return a successful response with a list of all reports.
        return ResponseEntity.ok(APIResponse.good(reports.getAllReports()));
    }

    /**
     * Deletes a report based on the provided report ID.
     *
     * @param request The HTTP request containing session information for the current user.
     * @param id The unique identifier of the report to be deleted.
     * @return A ResponseEntity containing an APIResponse confirming the deletion of the report.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteReport(
            HttpServletRequest request,
            @PathVariable String id) {
        // Retrieving the current user is currently commented out; user authorization is a consideration here.

        // Proceed to delete the report identified by the given ID.
        reports.deleteReport(id);

        // Return a successful response indicating that the deletion was processed.
        return ResponseEntity.ok(APIResponse.good(true));
    }
}
