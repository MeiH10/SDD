package Pucknotes.Server.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Session.SessionService;
import Pucknotes.Server.Verification.VerificationService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    // Inject the AccountService for account-related operations.
    @Autowired
    private AccountService service;

    // Inject the VerificationService for user verification.
    @Autowired
    private VerificationService verify_service;

    // Inject the SessionService for managing user sessions.
    @Autowired
    private SessionService sessions;

    // Create a new account using the provided registration token.
    @PostMapping("")
    public ResponseEntity<APIResponse<String>> createAccount(
            @RequestParam("token") String token,
            @RequestParam("registration") String registration) {

        // Verify the registration token and get the account details.
        Account details = verify_service.verifyToken(registration, token);
        // Register the account and return the account ID.
        Account account = service.registerAccount(details);
        return ResponseEntity.ok(APIResponse.good(account.getId()));
    }

    // Create a new account without token verification.
    @PostMapping("/force")
    public ResponseEntity<APIResponse<String>> forceAccount(
        @RequestParam(value = "email") String email,
        @RequestParam(value = "username") String username,
        @RequestParam(value = "password") String password) {

        // Create a new account and register it.
        Account account = new Account(email, username, password);
        service.registerAccount(account);
        
        // Return the account ID.
        return ResponseEntity.ok(APIResponse.good(account.getId()));
    }

    // Retrieve an account by its ID.
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Account>> getAccount(@PathVariable String id) {
        // Get the account by its ID.
        Account account = service.getById(id);
        return ResponseEntity.ok(APIResponse.good(account));
    }

    // Update the account information for the current user.
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "role", required = true) int role) {

        
        // Get the current user from the session.
        Account account = sessions.getCurrentUser(request);
        // Update the username and password.
        account.setUsername(username);
        account.setPassword(password);
        // Save the updated account.
        service.updateAccount(account, account.getId());
        // service.updateAccountRole(account, account, role);


        // Return a success message.
        return new ResponseEntity<>("Account updated successfully.", HttpStatus.OK);
    }

    // Delete the account for the current user.
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteAccount(
        HttpServletRequest request,
        @PathVariable String id) {
        try {
            // Get the current user from the session.
            Account account = sessions.getCurrentUser(request);
            // Delete the account.
            service.deleteAccount(account, account.getId());
            // Return a successful response.
            return ResponseEntity.ok(APIResponse.good(true));
        } catch (ResourceNotFoundException e) {
            // Return a failure response if the account is not found.
            return ResponseEntity.ok(APIResponse.good(false));
        }
    }
}
