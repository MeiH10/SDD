package Pucknotes.Server.Verification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Account.EmailService;
import Pucknotes.Server.Response.Types.ResourceConflictException;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VerificationService {
    @Autowired
    private VerificationRepository repository;
    private final EmailService email_service;
    private final AccountService account_service;

    public Verification createVerification(Account details) {
        if (details == null) {
            throw new IllegalArgumentException("Must create a verification on a valid account.");
        } else if (account_service.existsEmail(details.getEmail())) {
            throw new ResourceConflictException("Account with email '" + details.getEmail() + "' already exists.");
        }
        
        Verification verify = new Verification(details);
        verify = repository.save(verify);
        return verify;
    }

    public void sendEmail(String id) {
        Verification verify = getById(id);
        email_service.sendVerify(verify.getDetails().getEmail(), verify.getToken());
    }

    public Account verifyToken(String id, String token) {
        Verification verify = getById(id);
        if (!verify.getToken().equals(token)) {
            throw new IllegalArgumentException("Invalid verification token.");
        }

        return verify.getDetails();
    }

    public void deleteVerification(Verification verify) {
        if (verify == null) {
            return;
        }

        repository.delete(verify);
    }

    public Verification getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid verification ID.");
        }

        Verification verify = repository.findById(id).orElse(null);
        if (verify == null) {
            throw new ResourceNotFoundException("No verification with this ID.");
        }

        return verify;
    }

    public boolean existsId(String id) {
        if (id == null) {
            return false;
        }

        Verification verify = repository.findById(id).orElse(null);
        if (verify == null) {
            return false;
        }

        return true;
    }
}