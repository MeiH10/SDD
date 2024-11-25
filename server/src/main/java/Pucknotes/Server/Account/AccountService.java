package Pucknotes.Server.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Response.Types.ResourceConflictException;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Response.Types.UnauthorizedException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {
    @Autowired
    private AccountRepository repository;
    private final PasswordEncoder encoder;

    public Account registerAccount(String email, String username, String password) {
        //error handling if any inputs are empty and if the email doesnt exist
        if (email == null || username == null || password == null) {
            throw new IllegalArgumentException(
                    "Must specify non-null email, username, and password to create an account.");
        } else if (existsEmail(email)) {
            throw new ResourceConflictException("Account with email '" + email + "' already exists.");
        }
        //create a new account and save it
        Account account = new Account(email, username, encoder.encode(password));
        account = repository.save(account);
        return account;
    }

    public Account registerAccount(Account account) {
        return registerAccount(
            account.getEmail(), account.getUsername(), account.getPassword()
        );
    }

    public Account updateAccount(Account next, String userID) {
        //error handling if any inputs are empty and if the next account has the same userID
        if (next == null) {
            throw new IllegalArgumentException("Attempted to save a null account.");
        } else if (userID.equals(next.getId())) {
            throw new UnauthorizedException("You are not this user.");
        }
        //update account fields
        Account current = getById(next.getId());
        if (next.getUsername() != null)
            current.setUsername(next.getUsername());
        if (next.getPassword() != null)
            current.setPassword(next.getPassword());
        return repository.save(current);
    }

    public Account getByEmail(String email) {
        //error handling if email is empty
        if (email == null) {
            throw new IllegalArgumentException("Invalid account email.");
        }

        Account account = repository.findByEmail(email).orElse(null);
        //error handling if email doesnt exist
        if (account == null) {
            throw new ResourceNotFoundException("No account with this email.");
        }

        return account;
    }

    public boolean existsEmail(String email) {
        //error handling if email is empty
        if (email == null) {
            return false;
        }

        Account account = repository.findByEmail(email).orElse(null);
        //return true if account exists, else false
        return account != null;
    }

    public boolean existsById(String id) {
        return id != null && repository.existsById(id);
    }

    public Account getById(String id) {
        //error handling is id is empty
        if (id == null) {
            throw new IllegalArgumentException("Invalid account ID.");
        }

        Account account = repository.findById(id).orElse(null);
        //error handling is account doesnt exist
        if (account == null) {
            throw new ResourceNotFoundException("No account with this ID.");
        }

        return account;
    }

    public void deleteAccount(Account account, String userID) {
        //error handling is input is empty and if the next account has the same userID
        if (account == null) {
            return;
        } else if (userID.equals(account.getId())) {
            throw new UnauthorizedException("You are not this user.");
        }

        repository.delete(account);
    }
}
