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
    private final PasswordEncoder encoder;

    public Account registerAccount(String email, String username, String password) {
        if (email == null || username == null || password == null) {
            throw new IllegalArgumentException(
                    "Must specify non-null email, username, and password to create an account.");
        } else if (existsEmail(email)) {
            throw new ResourceConflictException("Account with email '" + email + "' already exists.");
        }
        
        Account account = new Account(email, username, encoder.encode(password));
        account = repository.save(account);
        return account;
    }

    public Account registerAccount(Account account) {
        return registerAccount(
            account.getEmail(), account.getUsername(), account.getPassword()
        );
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
        // if (next.getFirstname() != null)
        //     current.setFirstname(next.getFirstname());
        // if (next.getLastname() != null)
        //     current.setLastname(next.getLastname());

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

    public boolean existsEmail(String email) {
        if (email == null) {
            return false;
        }

        Account account = repository.findByEmail(email).orElse(null);
        return account != null;
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
