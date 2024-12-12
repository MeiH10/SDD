package Pucknotes.Server.Account;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * AccountRepository interface extends the MongoRepository interface provided by Spring Data MongoDB.
 * This interface defines the basic CRUD (Create, Read, Update, Delete) operations for the Account entity.
 * It also includes a custom method to find an account by email.
 */
public interface AccountRepository extends MongoRepository<Account, String> {
  /**
   * This method is used to find an Account entity by its email address.
   * @param email the email address of the account to be found
   * @return an Optional containing the found Account, or an empty Optional if no account is found
   */
  public Optional<Account> findByEmail(String email);
}
