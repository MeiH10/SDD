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

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping("")
    public ResponseEntity<APIResponse<String>> createAccount(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName) {

        Account account = service.registerAccount(email, username, password);
        account.setFirstname(firstName);
        account.setLastname(lastName);
        account = service.updateAccount(account);

        return ResponseEntity.ok(APIResponse.good(account.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Account>> getAccount(@PathVariable String id) {
        Account account = service.getByEmail(id);
        return ResponseEntity.ok(APIResponse.good(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(
            @PathVariable String id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "firstname", required = false) String firstName,
            @RequestParam(value = "lastname", required = false) String lastName) {

        Account account = service.getByEmail(id);
        account.setUsername(username);
        account.setPassword(password);
        account.setFirstname(firstName);
        account.setLastname(lastName);
        service.updateAccount(account);

        return new ResponseEntity<>("Account updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteAccount(@PathVariable String id) {
        try {
            Account account = service.getById(id);
            service.deleteAccount(account);
            return ResponseEntity.ok(APIResponse.good(true));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(APIResponse.good(false));
        }
    }
}