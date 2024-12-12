package Pucknotes.Server.Semester;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class represents a Semester entity in the application.
 * It is mapped to a MongoDB collection named "semesters".
 * A Semester object contains details about the academic semester,
 * including the season, year, and name of the semester.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "semesters")
public class Semester {

  /**
   * The unique identifier for the Semester.
   * This field is annotated with @Id to indicate that it is the primary key in the MongoDB document.
   */
  @Id
  @NonNull
  private String id;

  /**
   * The season of the semester, e.g., "Fall", "Spring", or "Summer".
   * This field is important for understanding the academic schedule.
   */
  private String season;

  /**
   * The year in which the semester takes place.
   * This field is of type Number to allow for flexibility in year representation.
   */
  private Number year;

  /**
   * The name of the semester, which may include additional context or identifiers.
   * This field provides a more descriptive identification of the semester.
   */
  @NonNull
  private String name;
}
