package Pucknotes.Server.Verification;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The VerificationController class serves as a REST controller that handles
 * registration verification processes, such as creating registrations,
 * resending verification emails, and deleting registrations.
 */
@RestController
@RequestMapping("/api/registration")
public class VerificationController {

  @Autowired
  private VerificationService service;

  /**
   * Creates a new registration for a user by capturing their
   * email, username, and password. It also initiates the
   * verification process by generating a verification entity
   * and sending a verification email to the provided email address.
   *
   * @param email    The email address of the user to register.
   * @param username The username that the user wishes to use.
   * @param password The password for the user’s account.
   * @return A ResponseEntity containing an APIResponse with the
   *         verification ID if successful.
   */
  @PostMapping("")
  public ResponseEntity<APIResponse<String>> createRegistration(
    @RequestParam(value = "email") String email,
    @RequestParam(value = "username") String username,
    @RequestParam(value = "password") String password
  ) {
    // Create an Account object using the provided details.
    Account account = new Account(email, username, password);
    // Initiate verification process for the created account.
    Verification verify = service.createVerification(account);
    // Send a verification email to the user.
    service.sendEmail(verify.getId());

    // Return a successful response containing the verification ID.
    return ResponseEntity.ok(APIResponse.good(verify.getId()));
  }

  /**
   * Resends a verification email to the user associated with
   * the provided verification ID. This is useful for users who
   * may have not received the initial verification email.
   *
   * @param id The ID of the verification to resend.
   * @return A ResponseEntity containing an APIResponse indicating
   *         the success of the resend operation.
   */
  @PutMapping("/{id}")
  public ResponseEntity<APIResponse<Boolean>> resendEmail(
    @PathVariable String id
  ) {
    // Resend the verification email to the user associated with the ID.
    service.sendEmail(id);
    // Return a successful response indicating the email was resent.
    return ResponseEntity.ok(APIResponse.good(true));
  }

  /**
   * Deletes a registration based on the provided verification ID.
   * This operation first checks if the ID exists before attempting
   * to delete the verification entry.
   *
   * @param id The ID of the verification to delete.
   * @return A ResponseEntity containing an APIResponse indicating
   *         the success of the delete operation.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<APIResponse<Boolean>> deleteRegistration(
    @PathVariable String id
  ) {
    // Check if the verification ID exists before proceeding with deletion.
    if (!service.existsId(id)) {
      return ResponseEntity.ok(APIResponse.good(false));
    }

    // Delete the verification entry if it exists.
    service.deleteVerification(service.getById(id));
    // Return a successful response indicating the registration was deleted.
    return ResponseEntity.ok(APIResponse.good(true));
  }
}
