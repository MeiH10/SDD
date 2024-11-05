package Pucknotes.Server.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Response.Types.ResourceConflictException;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {
    @Autowired
    private AccountRepository repository;
    private final EmailService email_service;
    private final PasswordEncoder encoder;

    public Account registerAccount(String email, String username, String password) {
        if (email == null || username == null || password == null) {
            throw new IllegalArgumentException(
                    "Must specify non-null email, username, and password to create an account.");
        } else if (getByEmail(email) != null) {
            throw new ResourceConflictException("Account with " + email + " already exists.");
        }

        Account account = new Account(email, username, encoder.encode(password));
        account.setEnabled(false);
        account = repository.save(account);

        email_service.sendVerify(account.getEmail(), account.getToken());
        return account;
    }

    public Account verifyEmail(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Invalid verification token.");
        }

        Account account = repository.findByToken(token).orElse(null);
        if (account == null) {
            throw new IllegalArgumentException("Invalid verification token.");
        }

        account.setEnabled(true);
        account.setToken(null);
        repository.save(account);
        return account;
    }

    public Account updateAccount(Account next) {
        if (next == null) {
            throw new IllegalArgumentException("Attempted to save a null account.");
        }

        Account current = getById(next.getId());
        if (next.getUsername() != null)
            current.setUsername(next.getUsername());
        if (next.getPassword() != null)
            current.setPassword(next.getPassword());
        if (next.getFirstname() != null)
            current.setFirstname(next.getFirstname());
        if (next.getLastname() != null)
            current.setLastname(next.getLastname());

        return repository.save(current);
    }

    public Account getByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Invalid account email.");
        }

        Account account = repository.findByEmail(email).orElse(null);
        if (account == null) {
            throw new ResourceNotFoundException("No account with this email.");
        }

        return account;
    }

    public Account getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid account ID.");
        }

        Account account = repository.findById(id).orElse(null);
        if (account == null) {
            throw new ResourceNotFoundException("No account with this ID.");
        }

        return account;
    }

    public void deleteAccount(Account account) {
        if (account == null) {
            return;
        }

        repository.delete(account);
    }
}
