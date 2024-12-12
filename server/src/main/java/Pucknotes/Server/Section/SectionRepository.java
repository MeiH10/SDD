package Pucknotes.Server.Section;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * SectionRepository is an interface that serves as a repository for managing Section entities.
 * It extends the MongoRepository interface provided by Spring Data, which offers generic CRUD operations
 * as well as the ability to perform database queries specific to Section entities.
 */
public interface SectionRepository extends MongoRepository<Section, String> {
  /**
   * Checks whether a Section entity exists in the database with the specified number.
   *
   * @param number the unique string identifier for the Section entity to check.
   * @return true if a Section with the specified number exists; false otherwise.
   * This method utilizes MongoDB's capabilities to quickly verify the existence of the entity.
   */
  boolean existsByNumber(String number);

  /**
   * Retrieves a Section entity from the database using the specified number.
   *
   * @param number the unique string identifier for the Section entity to retrieve.
   * @return the Section entity associated with the specified number, or null if it does not exist.
   * This method relies on the underlying Spring Data MongoDB implementation to fetch data efficiently.
   */
  Section getByNumber(String number);
}
