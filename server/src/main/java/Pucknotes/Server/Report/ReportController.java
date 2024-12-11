package Pucknotes.Server.Report;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Comment.Comment;
import Pucknotes.Server.Comment.CommentService;
import Pucknotes.Server.Note.Note;
import Pucknotes.Server.Note.NoteService;
import Pucknotes.Server.Response.APIResponse;
// import Pucknotes.Server.Response.Types.UnauthorizedException;
import Pucknotes.Server.Session.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @Autowired
  private NoteService notes;

  @Autowired
  private CommentService comments;

  /**
   * Creates a new report based on the provided title, description, and associated note.
   *
   * @param request The HTTP request containing session information for the current user.
   * @param title The title of the report to be created.
   * @param description A detailed description of the report.
   * @param noteId The unique identifier of the note associated with the report.
   * @return A ResponseEntity containing an APIResponse with the ID of the newly created report.
   */
  @PostMapping("")
  public ResponseEntity<APIResponse<String>> createReport(
    HttpServletRequest request,
    @RequestParam("title") String title,
    @RequestParam("description") String description,
    @RequestParam("type") String type,
    @RequestParam("item") String itemId
  ) {
    Account user = sessions.getCurrentUser(request);
    Report report = new Report();

    // Retrieve the note by its ID to associate it with the report.
    if (type.equals("note")) {
      Note note = notes.getById(itemId);
      report.setType(type);
      report.setItem(note.getId());
    } else if (type.equals("comment")) {
      Comment comment = comments.getById(itemId);
      report.setType(type);
      report.setItem(comment.getId());
    } else {
      throw new IllegalArgumentException(
        "You can only report a 'comment' or a 'note'."
      );
    }

    // Instantiate a new Report object to store the report data.
    report.setTitle(title);
    report.setDescription(description);
    report.setOwner(user.getId());

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
  public ResponseEntity<APIResponse<List<Report>>> getAllReports(
    HttpServletRequest request
  ) {
    Account account = sessions.getCurrentUser(request);
    List<Report> reportList = reports.getAllReports(account);
    return ResponseEntity.ok(APIResponse.good(reportList));
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
    @PathVariable String id
  ) {
    Account account = sessions.getCurrentUser(request);
    reports.deleteReport(id, account);
    return ResponseEntity.ok(APIResponse.good(true));
  }
}
