package SDD.PuckNotes.Account;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {
    // MongoRepository provides methods like existsById, findById, and save.
}