package Pucknotes.Server.School;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * The SchoolRepository interface serves as a repository for performing CRUD operations on 
 * School entities within the MongoDB database. This interface extends the MongoRepository 
 * interface provided by Spring Data, which enables easy data access and manipulation without 
 * the need for boilerplate code.
 */
public interface SchoolRepository extends MongoRepository<School, String> {
    
    /**
     * Retrieves an Optional containing the School entity that matches the given name.
     * 
     * @param name The name of the school to search for.
     * @return An Optional wrapping the School entity if found, or an empty Optional if no 
     *         matching School exists.
     * 
     * This method allows for safe handling of potentially missing data by returning an 
     * Optional type. One critical aspect is to ensure that the input string is not null 
     * or empty, as it will lead to unexpected behavior or exceptions.
     */
    Optional<School> findByName(String name);
    
    /**
     * Checks whether a School entity with the specified name exists within the database.
     * 
     * @param name The name of the school to check for existence.
     * @return true if a School with the specified name exists, false otherwise.
     * 
     * This method is useful for validating input data before attempting to create a new 
     * School entity with the same name. It is important to handle cases where the 
     * input name might be null or empty, which could result in unintended behaviors.
     */
    boolean existsByName(String name);
}
