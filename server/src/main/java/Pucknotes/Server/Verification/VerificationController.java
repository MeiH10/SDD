package Pucknotes.Server.Verification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Response.APIResponse;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/registration")
public class VerificationController {

    @Autowired
    private VerificationService service;

    @Autowired
    private AccountService accounts;

    @PostMapping("")
    public ResponseEntity<APIResponse<String>> createRegistration(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {

        Account account = new Account(email, username, password);
        Verification verify = service.createVerification(account);
        service.sendEmail(verify.getId());

        return ResponseEntity.ok(APIResponse.good(verify.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> resendEmail(
                @PathVariable String id) {

        service.sendEmail(id);
        return ResponseEntity.ok(APIResponse.good(true));
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<APIResponse<String>> verifyRegistration(
                @PathVariable String id,
                @RequestParam("token") String token) {

        Account details = service.verifyToken(id, token);
        Account account = accounts.registerAccount(details);
        return ResponseEntity.ok(APIResponse.good(account.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteRegistration(@PathVariable String id) {
        if (!service.existsId(id)) {
            return ResponseEntity.ok(APIResponse.good(false));
        }

        service.deleteVerification(service.getById(id));
        return ResponseEntity.ok(APIResponse.good(true));
    }
}