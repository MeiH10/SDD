package Pucknotes.Server.Semester;

import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

/**
 * The SemesterService class provides a service layer for managing semesters within the application.
 * It handles requests for retrieving semester information from a MongoDB repository, leveraging
 * Spring Data JPA features.
 */
@Service
@AllArgsConstructor
@Document(collection = "semesters")
public class SemesterService {

  @Autowired
  private SemesterRepository repository;

  /**
   * Retrieves a list of Semester objects filtered by year and season, and optionally sorted.
   *
   * @param year   The year to filter semesters by. A value of -1 indicates no filtering by year.
   * @param season The season to filter semesters by. A value of "any" indicates no filtering by season.
   * @param sort   A string indicating the sort order. Use "newest" for descending order by year.
   * @return A list of Semester objects matching the specified filters and sorting order.
   */
  public List<Semester> getSemesters(Integer year, String season, String sort) {
    Sort order = Sort.by(
      "newest".equalsIgnoreCase(sort)
        ? Sort.Order.desc("year")
        : Sort.Order.asc("year")
    );

    // Check if both year and season are not specified.
    if (year == -1 && season.equals("any")) {
      return repository.findAll(); // Return all semesters if no filters are applied.
    } else if (season != null && !season.equals("any") && year == -1) {
      return repository.findBySeason(season, order); // Filter by season only.
    } else if (season.equals("any") && year != -1) {
      return repository.findByYear(year, order); // Filter by year only.
    } else {
      return repository.findBySeasonAndYear(season, year, order); // Filter by both year and season.
    }
  }

  /**
   * Retrieves a Semester object from the repository by its unique identifier.
   *
   * @param id The unique identifier of the semester to retrieve.
   * @return The Semester object with the specified ID.
   * @throws IllegalArgumentException If the provided ID is null.
   * @throws ResourceNotFoundException If no semester is found with the specified ID.
   */
  public Semester getById(String id) {
    if (id == null) {
      throw new IllegalArgumentException("Invalid semester ID."); // Validate input.
    }

    Semester account = repository.findById(id).orElse(null);
    if (account == null) {
      throw new ResourceNotFoundException("No semester with this ID."); // Handle case where semester is not found.
    }

    return account; // Return the found semester.
  }

  /**
   * Retrieves a Semester object by its name.
   *
   * @param name The name of the semester to retrieve.
   * @return The Semester object with the specified name, or null if not found.
   */
  public Semester getByName(String name) {
    return repository.findByName(name).orElse(null); // Retrieve by name, returning null if not found.
  }

  /**
   * Checks if a Semester with the specified ID exists in the repository.
   *
   * @param id The unique identifier of the semester.
   * @return A boolean indicating whether the semester exists.
   */
  public boolean existsById(String id) {
    return repository.existsById(id); // Check for existence based on ID.
  }

  /**
   * Checks if a Semester with the specified name exists in the repository.
   *
   * @param name The name of the semester.
   * @return A boolean indicating whether the semester exists.
   */
  public boolean existsByName(String name) {
    return repository.existsByName(name); // Check for existence based on name.
  }
}
