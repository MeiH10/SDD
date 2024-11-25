package Pucknotes.Server.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Response.Types.ResourceConflictException;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Response.Types.UnauthorizedException;
import lombok.AllArgsConstructor;

/**
 * The AccountService class is responsible for managing user accounts within the application.
 * This class provides functionality for registering, updating, retrieving, and 
 * deleting user accounts, while ensuring that appropriate checks are made to maintain 
 * the integrity of account data.
 */
@Service
@AllArgsConstructor
public class AccountService {

    @Autowired
    private AccountRepository repository;

    private final PasswordEncoder encoder;

    /**
     * Registers a new user account with the specified email, username, and password.
     *
     * @param email the email address of the user, must be non-null.
     * @param username the username of the user, must be non-null.
     * @param password the password for the user account, must be non-null.
     * @return the newly created Account object.
     * @throws IllegalArgumentException if any of the parameters are null.
     * @throws ResourceConflictException if an account with the specified email already exists.
     */
    public Account registerAccount(String email, String username, String password) {
        // Error handling if any inputs are empty and if the email already exists.
        if (email == null || username == null || password == null) {
            throw new IllegalArgumentException(
                    "Must specify non-null email, username, and password to create an account.");
        } else if (existsEmail(email)) {
            throw new ResourceConflictException("Account with email '" + email + "' already exists.");
        }
        
        // Create a new account and save it.
        Account account = new Account(email, username, encoder.encode(password));
        account = repository.save(account);
        return account;
    }

    /**
     * Registers a new user account based on the provided Account object.
     *
     * @param account the Account object containing the user's details.
     * @return the newly created Account object.
     */
    public Account registerAccount(Account account) {
        return registerAccount(
            account.getEmail(), account.getUsername(), account.getPassword()
        );
    }

    /**
     * Updates an existing user account with new details specified in the next Account object.
     *
     * @param next the Account object containing updated user details.
     * @param userID the ID of the user attempting the update.
     * @return the updated Account object.
     * @throws IllegalArgumentException if the next account is null.
     * @throws UnauthorizedException if the userID does not match the ID of the account being updated.
     */
    public Account updateAccount(Account next, String userID) {
        // Error handling if the input account is null or if the userID matches the current account's ID
        if (next == null) {
            throw new IllegalArgumentException("Attempted to save a null account.");
        } else if (userID.equals(next.getId())) {
            throw new UnauthorizedException("You are not this user.");
        }
        
        // Update account fields.
        Account current = getById(next.getId());
        if (next.getUsername() != null)
            current.setUsername(next.getUsername());
        if (next.getPassword() != null)
            current.setPassword(next.getPassword());
        
        return repository.save(current);
    }

    /**
     * Retrieves an account by the specified email address.
     *
     * @param email the email address associated with the account.
     * @return the Account object corresponding to the specified email.
     * @throws IllegalArgumentException if the email parameter is null.
     * @throws ResourceNotFoundException if no account is found with the specified email.
     */
    public Account getByEmail(String email) {
        // Error handling if the email is empty.
        if (email == null) {
            throw new IllegalArgumentException("Invalid account email.");
        }

        Account account = repository.findByEmail(email).orElse(null);
        
        // Error handling if no account is found with the provided email.
        if (account == null) {
            throw new ResourceNotFoundException("No account with this email.");
        }

        return account;
    }

    /**
     * Checks if an account exists with the specified email address.
     *
     * @param email the email address to check.
     * @return true if an account exists with the specified email; false otherwise.
     */
    public boolean existsEmail(String email) {
        // Error handling if the email is empty.
        if (email == null) {
            return false;
        }

        Account account = repository.findByEmail(email).orElse(null);
        
        // Return true if the account exists; otherwise, return false.
        return account != null;
    }

    /**
     * Checks if an account exists with the specified ID.
     *
     * @param id the ID of the account.
     * @return true if the account exists with the specified ID; false otherwise.
     */
    public boolean existsById(String id) {
        return id != null && repository.existsById(id);
    }

    /**
     * Retrieves an account by the specified ID.
     *
     * @param id the ID of the account.
     * @return the Account object corresponding to the specified ID.
     * @throws IllegalArgumentException if the id parameter is null.
     * @throws ResourceNotFoundException if no account is found with the specified ID.
     */
    public Account getById(String id) {
        // Error handling if the ID is empty.
        if (id == null) {
            throw new IllegalArgumentException("Invalid account ID.");
        }

        Account account = repository.findById(id).orElse(null);
        
        // Error handling if no account is found with the provided ID.
        if (account == null) {
            throw new ResourceNotFoundException("No account with this ID.");
        }

        return account;
    }

    /**
     * Deletes an account based on the provided Account object and user ID.
     *
     * @param account the Account object to be deleted.
     * @param userID the ID of the user attempting to delete the account.
     * @throws UnauthorizedException if the user attempting to delete the account does not own it.
     */
    public void deleteAccount(Account account, String userID) {
        // Error handling if the account input is null and if the userID matches the account's ID.
        if (account == null) {
            return; // Simply return if there is no account to delete.
        } else if (userID.equals(account.getId())) {
            throw new UnauthorizedException("You are not this user.");
        }

        repository.delete(account);
    }
}
