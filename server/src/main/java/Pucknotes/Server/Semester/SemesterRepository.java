package Pucknotes.Server.Semester;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * The SemesterRepository interface provides methods to perform CRUD (Create, Read, Update, Delete) 
 * operations on Semester entities in a MongoDB database. It extends the Spring Data MongoDB
 * MongoRepository interface, which offers generic methods for database interaction.
 */
public interface SemesterRepository extends MongoRepository<Semester, String> {
    
    /**
     * Finds a list of Semester entities that match the specified season and year,
     * sorted according to the provided Sort parameter.
     *
     * @param season The season during which the semester takes place, represented as a String.
     * @param year The year in which the semester occurs, represented as an integer.
     * @param sort The sorting criteria to apply to the result set.
     * @return A list of Semester entities matching the specified season and year, sorted according
     *         to the provided criteria.
     */
    List<Semester> findBySeasonAndYear(String season, int year, Sort sort);

    /**
     * Finds a list of Semester entities that match the specified season,
     * sorted according to the provided Sort parameter.
     *
     * @param season The season during which the semester takes place, represented as a String.
     * @param sort The sorting criteria to apply to the result set.
     * @return A list of Semester entities matching the specified season, sorted according
     *         to the provided criteria.
     */
    List<Semester> findBySeason(String season, Sort sort);

    /**
     * Finds a list of Semester entities that correspond to the given year,
     * sorted according to the provided Sort parameter.
     *
     * @param year The year in which the semester occurs, represented as an integer.
     * @param sort The sorting criteria to apply to the result set.
     * @return A list of Semester entities matching the specified year, sorted according
     *         to the provided criteria.
     */
    List<Semester> findByYear(int year, Sort sort);

    /**
     * Finds all Semester entities present in the database, sorted according to the provided Sort parameter.
     *
     * @param sort The sorting criteria to apply to the result set.
     * @return A list of all Semester entities, sorted according to the provided criteria.
     */
    List<Semester> findAll(Sort sort);
    
    /**
     * Retrieves a Semester entity by its unique identifier (ID).
     *
     * @param id The unique identifier of the Semester entity, represented as a String.
     * @return An Optional containing the Semester entity if found, or empty if not found.
     */
    Optional<Semester> findById(String id);

    /**
     * Retrieves a Semester entity by its name.
     *
     * @param name The name of the Semester entity, represented as a String.
     * @return An Optional containing the Semester entity if found, or empty if not found.
     */
    Optional<Semester> findByName(String name);

    /**
     * Checks if a Semester entity exists with the specified name.
     *
     * @param name The name of the Semester entity, represented as a String.
     * @return True if a Semester entity exists with the specified name; otherwise, false.
     */
    boolean existsByName(String name);
}
