package Pucknotes.Server.Session;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The SessionService class is responsible for managing user sessions
 * within the application. It provides functionality for creating,
 * deleting, and retrieving sessions, as well as obtaining the currently
 * logged-in user based on the session information.
 */
@Service
public class SessionService {

  @Autowired
  private AccountService accountService;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  /**
   * Creates a user session by validating the provided email and password.
   * If the credentials are valid, the user ID is stored in the session.
   *
   * @param request an HttpServletRequest object that contains the request the client made
   * @param email the email of the user attempting to create a session
   * @param password the password provided by the user for authentication
   * @return the ID of the user if the session is created successfully
   * @throws IllegalArgumentException if the email and password do not match the stored values
   */
  public String createSession(
    HttpServletRequest request,
    String email,
    String password
  ) {
    Account account = accountService.getByEmail(email);

    // Verify if the retrieved account exists. If it does not, an exception should be thrown.
    if (account == null) {
      throw new IllegalArgumentException(
        "No account found with the specified email."
      );
    }

    // Check if the provided password matches the stored password for the account.
    if (!passwordEncoder.matches(password, account.getPassword())) {
      throw new IllegalArgumentException("Password and email do not match.");
    }

    // Store the user's ID in the session upon successful authentication.
    request.getSession().setAttribute("id", account.getId());
    return account.getId();
  }

  /**
   * Deletes the current user session by invalidating it.
   *
   * @param request an HttpServletRequest object that contains the request the client made
   */
  public void deleteSession(HttpServletRequest request) {
    // Invalidating the session effectively logs out the user.
    request.getSession().invalidate();
  }

  /**
   * Retrieves the session ID of the current user.
   *
   * @param request an HttpServletRequest object that contains the request the client made
   * @return the session ID as a String, or null if there is no session
   */
  public String getSession(HttpServletRequest request) {
    return (String) request.getSession().getAttribute("id");
  }

  /**
   * Obtains the currently logged-in user based on the session information.
   * If the session does not correspond to an existing user, null is returned.
   *
   * @param request an HttpServletRequest object that contains the request the client made
   * @return an Account object representing the current user, or null if not found
   */
  public Account getCurrentUser(HttpServletRequest request) {
    String id = getSession(request);
    try {
      return accountService.getById(id);
    } catch (ResourceNotFoundException error) {
      // If the user with the given ID cannot be found, return null.
      return null;
    }
  }
}
