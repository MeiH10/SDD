package SDD.PuckNotes.Account;

import SDD.PuckNotes.Notes.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;


import java.io.IOException;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    // Endpoint to add a new account
    @Autowired
    private AccountService accountService;

    // Endpoint to add a new account
    @PostMapping("/add")
    public ResponseEntity<String> addAccount(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName) {
        try {
            Account account;
            if (firstName != null && lastName != null) {
                account = new Account(username, password, email, firstName, lastName);
            } else {
                account = new Account(username, password, email);
            }
            accountService.saveAccount(account);
            return new ResponseEntity<>("Account created successfully.", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while creating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PutMapping("/update")
    public ResponseEntity<String> updateAccount(
            @RequestParam("email") String email,  // Email as the ID
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName) {
        try {
   
            Account account = accountService.findAccountByEmail(email);

     
            if (username != null) account.setUsername(username);
            if (password != null) account.setPassword(password);
            if (firstName != null) account.setFirstName(firstName);
            if (lastName != null) account.setLastName(lastName);

     
            accountService.saveAccount(account);

            return new ResponseEntity<>("Account updated successfully.", HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>("Account not found with email: " + email, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while updating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}