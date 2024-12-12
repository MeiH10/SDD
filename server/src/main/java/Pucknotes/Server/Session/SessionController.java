package Pucknotes.Server.Session;

import Pucknotes.Server.Response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The SessionController class handles HTTP requests related to user session management.
 * It provides RESTful endpoints for creating, deleting, and retrieving user sessions.
 */
@RestController
@RequestMapping("/api/session")
public class SessionController {

  @Autowired
  private SessionService sessionService;

  /**
   * Creates a user session based on the provided email and password.
   *
   * @param request The HttpServletRequest object that provides request information
   *                for HTTP servlets. This is often used for session validation.
   * @param email The email of the user attempting to create a session.
   * @param password The password of the user attempting to create a session.
   * @return A ResponseEntity containing an APIResponse with the user's account ID
   *         if the session is created successfully.
   */
  @PostMapping("")
  public ResponseEntity<APIResponse<String>> createSession(
    HttpServletRequest request,
    @RequestParam String email,
    @RequestParam String password
  ) {
    // The following line calls the session service to create a new session
    // based on the email and password provided. Ensure proper validation and
    // error handling are managed within the service.
    String accountId = sessionService.createSession(request, email, password);

    // Returning a successful response encapsulated in APIResponse.
    return ResponseEntity.ok(APIResponse.good(accountId));
  }

  /**
   * Deletes the user session associated with the provided request.
   *
   * @param request The HttpServletRequest object that provides request information
   *                for HTTP servlets. This is often used for identifying the session.
   * @return A ResponseEntity containing an APIResponse indicating the success of
   *         the operation.
   */
  @DeleteMapping("")
  public ResponseEntity<APIResponse<Boolean>> deleteSession(
    HttpServletRequest request
  ) {
    // The session service is called to perform the deletion of the session.
    sessionService.deleteSession(request);

    // Returning a response that indicates the deletion operation was successful.
    return ResponseEntity.ok(APIResponse.good(false));
  }

  /**
   * Retrieves the account ID associated with the current user session.
   *
   * @param request The HttpServletRequest object that provides request information
   *                for HTTP servlets. This is used to access session details.
   * @return A ResponseEntity containing an APIResponse with the user's account ID
   *         if the session exists.
   */
  @GetMapping("")
  public ResponseEntity<APIResponse<String>> getSession(
    HttpServletRequest request
  ) {
    // The session service is called to retrieve the account ID associated with the session.
    String accountId = sessionService.getSession(request);

    // Returning a successful response encapsulated in APIResponse.
    return ResponseEntity.ok(APIResponse.good(accountId));
  }
}
