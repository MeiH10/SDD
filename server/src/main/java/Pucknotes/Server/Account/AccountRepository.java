package Pucknotes.Server.Account;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {
    public Optional<Account> findByEmail(String email);
}