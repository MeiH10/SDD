package Pucknotes.Server.Section;

import Pucknotes.Server.Response.Types.ResourceNotFoundException;
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
 * This service class provides methods to interact with sections in the database.
 * It contains functions to retrieve sections, check their existence, and fetch
 * them by their ID or number.
 */
@Service
@AllArgsConstructor
@Document(collection = "sections")
public class SectionService {

  @Autowired
  private SectionRepository repository;

  @Autowired
  private MongoTemplate template;

  /**
   * Retrieves a list of sections based on the specified course ID.
   * Sections can be sorted by specified fields and in ascending or
   * descending order.
   *
   * @param courseId  The ID of the course to filter sections.
   * @param sortType  The field by which to sort the sections (e.g., "name" or "semester").
   * @param orderType The order direction for sorting ("asc" for ascending or "desc" for descending).
   * @return A list of sections that belong to the specified course.
   */
  public List<Section> getSections(
    String courseId,
    String sortType,
    String orderType
  ) {
    Query query = new Query();

    Sort.Direction direction = "asc".equalsIgnoreCase(orderType)
      ? Sort.Direction.ASC
      : Sort.Direction.DESC;

    switch (sortType.toLowerCase()) {
      case "name":
        query.with(Sort.by(direction, "name"));
        break;
      case "semester":
        query.with(Sort.by(direction, "semester.year"));
        break;
      // If an unsupported sortType is provided, the query will not be modified.
      default:
        // Consider logging here if unsupported sortType is a frequent issue.
        break;
    }

    if (courseId != null) {
      query.addCriteria(Criteria.where("course").is(new ObjectId(courseId)));
    }

    return template.find(query, Section.class);
  }

  /**
   * Retrieves a section by its ID.
   *
   * @param id The ID of the section to retrieve.
   * @return The section associated with the given ID.
   * @throws IllegalArgumentException If the provided ID is null.
   * @throws ResourceNotFoundException If no section with the specified ID exists.
   */
  public Section getById(String id) {
    if (id == null) {
      throw new IllegalArgumentException("Invalid section ID.");
    }

    Section section = repository.findById(id).orElse(null);
    if (section == null) {
      throw new ResourceNotFoundException("No section with this ID.");
    }

    return section;
  }

  /**
   * Checks if a section exists with the specified ID.
   *
   * @param id The ID of the section to check for existence.
   * @return True if a section with the specified ID exists, otherwise false.
   */
  public boolean existsById(String id) {
    return repository.existsById(id);
  }

  /**
   * Checks if a section exists with the specified number.
   *
   * @param number The number of the section to check for existence.
   * @return True if a section with the specified number exists, otherwise false.
   */
  public boolean existsByNumber(String number) {
    return repository.existsByNumber(number);
  }

  /**
   * Retrieves a section by its number.
   *
   * @param number The number of the section to retrieve.
   * @return The section associated with the given number.
   */
  public Section getByNumber(String number) {
    return repository.getByNumber(number);
  }
}
