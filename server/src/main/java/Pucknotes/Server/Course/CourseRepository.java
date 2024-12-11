package Pucknotes.Server.Course;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CourseRepository is an interface that extends the MongoRepository
 * interface for the Course entity. It provides methods to perform
 * CRUD operations and additional queries specific to courses.
 * This repository is responsible for interacting with the MongoDB
 * database to manage Course entities.
 */
public interface CourseRepository extends MongoRepository<Course, String> {
    
    /**
     * Finds a Course entity by its unique code.
     *
     * @param code the unique code of the Course to be found.
     * @return an Optional containing the Course if found, otherwise
     *         an empty Optional.
     *         
     * This method allows developers to easily retrieve a specific
     * Course from the database based on its code, which is crucial 
     * for operations where the course is identified by its unique
     * code instead of its ID.
     */
    Optional<Course> findByCode(String code);
    
    /**
     * Checks whether a Course with a specific code exists in the database.
     *
     * @param code the unique code of the Course to check for existence.
     * @return true if a Course with the specified code exists; 
     *         false otherwise.
     *         
     * This method is useful for validation checks before creating 
     * or updating a Course to ensure there are no conflicts with 
     * existing records.
     */
    boolean existsByCode(String code);
}
