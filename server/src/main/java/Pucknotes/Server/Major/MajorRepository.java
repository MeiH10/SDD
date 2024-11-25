package Pucknotes.Server.Major;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import Pucknotes.Server.School.School;

/**
 * The MajorRepository interface extends the MongoRepository interface
 * to provide CRUD operations on Major entities in a MongoDB database.
 * 
 * It includes methods to find a school by its code and to check if a
 * school exists based on its code.
 */
public interface MajorRepository extends MongoRepository<Major, String> {

    /**
     * Retrieves an {@link Optional} containing a School entity that matches
     * the provided code, if it exists. If no school is found, an empty
     * Optional is returned.
     *
     * @param code a String code representing the unique identifier of a School.
     * @return an Optional containing the School if found, or an empty Optional otherwise.
     */
    Optional<School> findByCode(String code);

    /**
     * Checks whether a School entity exists in the repository with a 
     * specified code.
     *
     * @param code a String code that represents the unique identifier of a School.
     * @return true if a School with the specified code exists, otherwise false.
     */
    boolean existsByCode(String code);
}
