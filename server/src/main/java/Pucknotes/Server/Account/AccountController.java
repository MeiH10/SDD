package Pucknotes.Server.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Session.SessionService;
import Pucknotes.Server.Verification.VerificationService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService service;

    @Autowired
    private VerificationService verify_service;

    @Autowired
    private SessionService sessionService;

    private String getCurrentUserId(HttpServletRequest request) {
        return sessionService.getSession(request);
    }

    @PostMapping("")
    public ResponseEntity<APIResponse<String>> createAccount(
            @RequestParam("token") String token,
            @RequestParam("registration") String registration) {

        Account details = verify_service.verifyToken(registration, token);
        Account account = service.registerAccount(details);
        return ResponseEntity.ok(APIResponse.good(account.getId()));
    }

    @PostMapping("/force")
    public ResponseEntity<APIResponse<String>> forceAccount(
        @RequestParam(value = "email") String email,
        @RequestParam(value = "username") String username,
        @RequestParam(value = "password") String password) {

        Account account = new Account(email, username, password);
        service.registerAccount(account);
        
        return ResponseEntity.ok(APIResponse.good(account.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Account>> getAccount(@PathVariable String id) {
        Account account = service.getById(id);
        return ResponseEntity.ok(APIResponse.good(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password) {
        
        String userID = getCurrentUserId(request);
        Account account = service.getByEmail(id);
        account.setUsername(username);
        account.setPassword(password);
        service.updateAccount(account, userID);

        return new ResponseEntity<>("Account updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteAccount(
        HttpServletRequest request,
        @PathVariable String id) {
        try {
            String userID = getCurrentUserId(request);
            Account account = service.getById(id);
            service.deleteAccount(account, userID);
            return ResponseEntity.ok(APIResponse.good(true));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(APIResponse.good(false));
        }
    }
}