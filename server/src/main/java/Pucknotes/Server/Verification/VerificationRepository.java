package Pucknotes.Server.Verification;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * VerificationRepository is an interface that serves as a data access layer
 * for Verification entities. It extends the MongoRepository interface,
 * providing standard methods for CRUD operations and additional query methods
 * specific to the Verification entity.
 */
public interface VerificationRepository
  extends MongoRepository<Verification, String> {
  /**
   * Retrieves a Verification entity from the database based on the specified token.
   *
   * @param token the token used to find the corresponding Verification entity.
   * @return an Optional containing the Verification entity if found,
   *         or an empty Optional if no entity with the specified token exists.
   *
   * It is important to note that the token should be unique for each Verification
   * entity to ensure that this method functions correctly. Additionally,
   * consider implementing proper error handling in the calling methods to
   * address cases where the token is invalid or not present.
   */
  public Optional<Verification> findByToken(String token);
}
