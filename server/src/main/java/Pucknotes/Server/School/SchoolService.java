package Pucknotes.Server.School;

import java.util.List;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * The SchoolService class provides functionality to interact with the
 * School entity within a MongoDB database. This service includes methods
 * to retrieve schools by their ID or name, check for their existence,
 * and query schools based on specific criteria such as name and semester.
 * The results can be sorted based on specified parameters.
 */
@Service
@AllArgsConstructor
@Document(collection = "schools")
public class SchoolService {

  @Autowired
  private final SchoolRepository repository;

  @Autowired
  private MongoTemplate template;

  /**
   * Retrieves a School entity by its unique identifier.
   *
   * @param id The unique identifier of the School.
   * @return The School object if found, otherwise null.
   */
  public School getById(String id) {
    return repository.findById(id).orElse(null);
  }

  /**
   * Retrieves a School entity by its name.
   *
   * @param name The name of the School.
   * @return The School object if found, otherwise null.
   */
  public School getByName(String name) {
    return repository.findByName(name).orElse(null);
  }

  /**
   * Checks whether a School entity exists given its unique identifier.
   *
   * @param id The unique identifier of the School.
   * @return True if the School exists, otherwise false.
   */
  public boolean existsById(String id) {
    return repository.existsById(id);
  }

  /**
   * Checks whether a School entity exists given its name.
   *
   * @param name The name of the School.
   * @return True if the School exists, otherwise false.
   */
  public boolean existsByName(String name) {
    return repository.existsByName(name);
  }

  /**
   * Retrieves a list of School entities based on the provided criteria.
   *
   * This method can filter the results based on the name and semester ID,
   * and allows for sorting the results by different attributes such as
   * name or semester year in either ascending or descending order.
   *
   * @param name       The name of the School to search for. Passing null indicates no filtering by name.
   * @param semesterID The semester identifier to filter by. Passing null indicates no filtering by semester.
   * @param sortType   The attribute by which to sort the results.
   *                   It can be "name" or "semester".
   * @param orderType  The order of sorting; can be "asc" for ascending or "desc" for descending.
   * @return A list of matching School objects, sorted according to the provided criteria.
   */
  public List<School> getSchool(
    String name,
    String semesterID,
    String sortType,
    String orderType
  ) {
    Query query = new Query();

    // Determine the sorting direction based on the orderType parameter.
    Sort.Direction direction = "asc".equalsIgnoreCase(orderType)
      ? Sort.Direction.ASC
      : Sort.Direction.DESC;

    // Set the sort criteria based on the sortType parameter.
    switch (sortType.toLowerCase()) {
      case "name":
        query.with(Sort.by(direction, "name"));
        break;
      case "semester":
        query.with(Sort.by(direction, "semester.year"));
        break;
      // Additional cases can be added here for more sorting options.
    }

    // Add criteria to filter by semester ID if provided
    if (semesterID != null) {
      query.addCriteria(
        Criteria.where("semester").is(new ObjectId(semesterID))
      );
    }

    // Add criteria to filter by name using a case-insensitive regex if provided.
    if (name != null) {
      query.addCriteria(Criteria.where("name").regex(name, "i"));
    }

    // Execute the query against the School collection and return the results.
    return template.find(query, School.class);
  }
}
