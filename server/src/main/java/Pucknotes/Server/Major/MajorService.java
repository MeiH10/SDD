package Pucknotes.Server.Major;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.School.School;
import lombok.AllArgsConstructor;

/**
 * MajorService is responsible for managing major entities.
 * It contains methods to retrieve and check the existence of majors
 * while leveraging the underlying MongoDB repository.
 */
@Service
@AllArgsConstructor
@Document(collection = "majors")
public class MajorService {

    @Autowired
    private MajorRepository repository;

    @Autowired
    private MongoTemplate template;

    /**
     * Retrieves a list of Major entities based on the provided 
     * criteria of semester ID, school ID, name, sort type, and order type.
     * 
     * @param semesterID the ID of the semester to filter by.
     * @param schoolID the ID of the school to filter by.
     * @param name the name of the major to filter by, case insensitive.
     * @param sortType the attribute by which to sort the results.
     * @param orderType the direction of sorting, either "asc" or "desc".
     * @return a list of Majors matching the criteria.
     */
    public List<Major> getMajors(
            String semesterID,
            String schoolID,
            String name,
            String sortType,
            String orderType) {

        // Create a new query object to build the search criteria.
        Query query = new Query();
        
        // Determine the sorting direction based on the orderType parameter.
        Sort.Direction direction = "asc".equalsIgnoreCase(orderType)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        // Apply sorting based on the specified sortType.
        switch (sortType.toLowerCase()) {
            case "name":
                query.with(Sort.by(direction, "name"));
                break;
            case "semester":
                query.with(Sort.by(direction, "semester.year"));
                break;
            // Add more sort types as required in the future.
        }

        // Filter by semester ID, if provided.
        if (semesterID != null) {
            query.addCriteria(Criteria.where("semester").is(new ObjectId(semesterID)));
        }

        // Filter by school ID, if provided.
        if (schoolID != null) {
            query.addCriteria(Criteria.where("school").is(new ObjectId(schoolID)));
        }

        // Filter by name using regex for case insensitive matching.
        if (name != null) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }

        // Return the list of majors that match the query criteria.
        return template.find(query, Major.class);
    }

    /**
     * Retrieves a Major entity by its unique identifier.
     * 
     * @param id the unique identifier of the major.
     * @return the Major object if found.
     * @throws IllegalArgumentException if the provided ID is null.
     * @throws ResourceNotFoundException if no major with the provided ID exists.
     */
    public Major getById(String id) {
        // Validate the ID parameter; it cannot be null.
        if (id == null) {
            throw new IllegalArgumentException("Invalid major ID.");
        }

        // Attempt to find the major with the given ID.
        Major major = repository.findById(id).orElse(null);
        // If major is not found, throw an exception.
        if (major == null) {
            throw new ResourceNotFoundException("No major with this ID.");
        }

        // Return the found major.
        return major;
    }

    /**
     * Retrieves a School entity by its unique code.
     * 
     * @param code the unique code of the school.
     * @return the School object if found, null otherwise.
     */
    public School getByCode(String code) {
        // Return the school found by its code or null if not found.
        return repository.findByCode(code).orElse(null);
    }

    /**
     * Checks if a Major entity exists by its unique identifier.
     * 
     * @param id the unique identifier of the major.
     * @return true if the major exists, false otherwise.
     */
    public boolean existsById(String id) {
        // Utilize the repository to check existence of the major.
        return repository.existsById(id);
    }

    /**
     * Checks if a Major entity exists by its unique code.
     * 
     * @param code the unique code of the major.
     * @return true if the major exists, false otherwise.
     */
    public boolean existsByCode(String code) {
        // Utilize the repository to check existence of the major by code.
        return repository.existsByCode(code);
    }
}
