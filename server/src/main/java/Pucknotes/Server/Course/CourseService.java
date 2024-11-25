package Pucknotes.Server.Course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import lombok.AllArgsConstructor;

/**
 * The CourseService class provides service functionalities for managing course-related operations.
 * It interacts with the MongoDB database to retrieve, create, update, and delete courses.
 */
@Service
@AllArgsConstructor
@Document(collection = "courses")
public class CourseService {
    @Autowired
    private CourseRepository repository;

    @Autowired
    private MongoTemplate template;

    /**
     * Retrieves a list of courses based on specified filtering and sorting criteria.
     * 
     * @param majorID The identifier for the major of the courses to be retrieved.
     * @param schoolID The identifier for the school of the courses to be retrieved.
     * @param semesterID The identifier for the semester of the courses to be retrieved.
     * @param name The name or partial name of the courses to be retrieved.
     * @param sortType The attribute by which the results should be sorted (e.g., "name" or "semester").
     * @param orderType The order of the sorting, can be either "asc" for ascending or "desc" for descending.
     * @return A list of Course objects that match the specified criteria.
     */
    public List<Course> getCourses(String majorID, String schoolID, String semesterID, String name, String sortType, String orderType) {
        Query query = new Query();

        // Determine the sorting direction based on the orderType input.
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
            // Additional sorting criteria can be added here as necessary.
        }

        // Add criteria to the query based on the provided identifiers.
        if (majorID != null) {
            query.addCriteria(Criteria.where("major").is(majorID));
        }
        
        if (semesterID != null) {
            query.addCriteria(Criteria.where("semester").is(semesterID));
        }

        if (schoolID != null) {
            query.addCriteria(Criteria.where("school").is(schoolID));
        }

        if (name != null) {
            query.addCriteria(Criteria.where("name").regex(name, "i")); // The "i" flag denotes case-insensitive matching.
        }

        // Execute the query and return the found courses.
        return template.find(query, Course.class);
    }

    /**
     * Retrieves a single Course object by its unique identifier.
     * If the identifier is null or no course is found, appropriate exceptions are thrown.
     * 
     * @param id The unique identifier of the course to retrieve.
     * @return The Course object corresponding to the provided id.
     * @throws IllegalArgumentException If the provided id is null.
     * @throws ResourceNotFoundException If no course is found with the specified id.
     */
    public Course getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid course ID.");
        }

        // Attempt to find the course by id; throw exception if not found.
        Course section = repository.findById(id).orElse(null);
        if (section == null) {
            throw new ResourceNotFoundException("No course with this ID.");
        }

        return section;
    }

    /**
     * Retrieves a Course object based on its unique code.
     * 
     * @param code The unique code of the course to retrieve.
     * @return The Course object if found, otherwise returns null.
     */
    public Course getByCode(String code) {
        return repository.findByCode(code).orElse(null);
    }

    /**
     * Checks if a course exists in the repository based on its unique identifier.
     * 
     * @param id The unique identifier of the course to check.
     * @return true if the course exists; otherwise, false.
     */
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    /**
     * Checks if a course exists in the repository based on its unique code.
     * 
     * @param code The unique code of the course to check.
     * @return true if the course exists; otherwise, false.
     */
    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }
}
