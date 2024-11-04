package Pucknotes.Server.Account;

// import Pucknotes.Server.Notes.Note;
// import Pucknotes.Server.Notes.NoteRepository;
// import org.bson.BsonBinarySubType;
// import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

// import java.io.IOException;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    public void saveAccount(Account account) {
        if (accountRepo.existsById(account.getEmail())) {
            Account existingAccount = accountRepo.findById(account.getEmail()).orElseThrow();
            if (account.getUsername() != null) existingAccount.setUsername(account.getUsername());
            if (account.getPassword() != null) existingAccount.setPassword(account.getPassword());
            if (account.getFirstName() != null) existingAccount.setFirstName(account.getFirstName());
            if (account.getLastName() != null) existingAccount.setLastName(account.getLastName());
            accountRepo.save(existingAccount);
        } else {
            accountRepo.save(account);
        }
    }

    public Account findAccountByEmail(String email) {
        return accountRepo.findById(email).orElseThrow(() -> new AccountNotFoundException("Account with email " + email + " not found."));
    }

}
