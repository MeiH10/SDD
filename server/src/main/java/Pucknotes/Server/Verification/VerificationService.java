package Pucknotes.Server.Verification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Response.Types.ResourceConflictException;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import lombok.AllArgsConstructor;

/**
 * VerificationService is a service used for handling email verification
 * processes. It is responsible for creating verification tokens, sending
 * verification emails to users, and verifying tokens.
 */
@Service
@AllArgsConstructor
public class VerificationService {

    @Autowired
    private JavaMailSender mailer;

    @Autowired
    private VerificationRepository repository;
    
    private final AccountService account_service;

    /**
     * Creates a new verification object for the provided account details.
     * 
     * @param details The account information for which the verification is being created.
     * @return The created Verification object.
     * @throws IllegalArgumentException if the provided account details are null.
     * @throws ResourceConflictException if an account with the same email already exists.
     */
    public Verification createVerification(Account details) {
        if (details == null) {
            throw new IllegalArgumentException("Must create a verification on a valid account.");
        } else if (account_service.existsEmail(details.getEmail())) {
            throw new ResourceConflictException("Account with email '" + details.getEmail() + "' already exists.");
        }
        
        // Create a new verification instance for the provided account details.
        Verification verify = new Verification(details);
        
        // Persist the verification object to the repository.
        verify = repository.save(verify);
        return verify;
    }

    /**
     * Sends a verification email to the specified recipient with the provided token.
     * 
     * @param recipient The email address to which the verification email will be sent.
     * @param token The verification token to include in the email.
     */
    public void sendVerify(String recipient, String token) {
        // Construct the link for email verification.
        String link = UriComponentsBuilder
                .fromUriString("http://localhost:8080/api/auth/verify-email")
                .queryParam("token", token)
                .toUriString();

        // Create a simple mail message object.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject("Email Verification");
        message.setText("Verify your account by clicking on this link: " + link);

        // Log the details of the email being sent for debugging purposes.
        System.out.println("Sending email to: " + recipient);
        System.out.println("Verification link: " + link);

        // Send the email using the mailer service.
        mailer.send(message);
    }

    /**
     * Sends a verification email to the account linked with the specified verification ID.
     * 
     * @param id The ID of the verification instance.
     */
    public void sendEmail(String id) {
        // Retrieve the verification instance by its ID.
        Verification verify = getById(id);
        
        // Send the verification email using the retrieved verification details.
        sendVerify(verify.getDetails().getEmail(), verify.getToken());
    }

    /**
     * Verifies the provided token against the verification record associated with the specified ID.
     * 
     * @param id The ID of the verification instance.
     * @param token The token to verify.
     * @return The Account associated with the verified token.
     * @throws IllegalArgumentException if the provided token is invalid.
     */
    public Account verifyToken(String id, String token) {
        // Fetch the verification instance using its ID.
        Verification verify = getById(id);
        
        // Check if the provided token matches the token in the verification instance.
        if (!verify.getToken().equals(token)) {
            throw new IllegalArgumentException("Invalid verification token.");
        }

        // Return the associated account details upon successful verification.
        return verify.getDetails();
    }

    /**
     * Deletes the specified verification instance if it is not null.
     * 
     * @param verify The verification instance to delete.
     */
    public void deleteVerification(Verification verify) {
        // Check if the verification instance is null before attempting to delete.
        if (verify == null) {
            return; // No action is needed if the verification instance is null.
        }

        // Remove the verification instance from the repository.
        repository.delete(verify);
    }

    /**
     * Retrieves a verification instance by its ID.
     * 
     * @param id The ID of the verification instance to retrieve.
     * @return The Verification object associated with the given ID.
     * @throws IllegalArgumentException if the provided ID is null.
     * @throws ResourceNotFoundException if no verification instance is found with the given ID.
     */
    public Verification getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid verification ID.");
        }

        // Attempt to find the verification instance in the repository.
        Verification verify = repository.findById(id).orElse(null);
        if (verify == null) {
            throw new ResourceNotFoundException("No verification with this ID.");
        }

        // Return the found verification instance.
        return verify;
    }

    /**
     * Checks whether a verification instance exists for a given ID.
     * 
     * @param id The ID to check for existence.
     * @return True if a verification instance exists, otherwise false.
     */
    public boolean existsId(String id) {
        if (id == null) {
            return false; // Return false for null IDs.
        }

        // Check if there exists a verification instance with the given ID.
        Verification verify = repository.findById(id).orElse(null);
        return verify != null; // Return true if found, false otherwise.
    }
}
