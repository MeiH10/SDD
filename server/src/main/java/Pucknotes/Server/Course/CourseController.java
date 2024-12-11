package Pucknotes.Server.Course;

import Pucknotes.Server.Major.MajorService;
import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.School.SchoolService;
import Pucknotes.Server.Semester.SemesterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/course")
public class CourseController {

  @Autowired
  private CourseService courses;

  @Autowired
  private MajorService majors;

  @Autowired
  private SchoolService schools;

  @Autowired
  private SemesterService semesters;

  /**
   * Retrieves a list of courses based on various optional parameters,
   * including major identifiers, school identifiers, semester identifiers, and sorting options.
   *
   * @param name The name of the course to search for. Optional.
   * @param majorCode The code of the major to filter by. Optional.
   * @param majorID The ID of the major to filter by. Optional.
   * @param schoolName The name of the school to filter by. Optional.
   * @param schoolID The ID of the school to filter by. Optional.
   * @param semesterName The name of the semester to filter by. Optional.
   * @param semesterID The ID of the semester to filter by. Optional.
   * @param sort The field to sort results by. Defaults to "name".
   * @param order The order of sorting, either "asc" for ascending or "desc" for descending. Defaults to "asc".
   * @param type Specifies the type of response expected: either "object", "count", or "id". Defaults to "id".
   * @return A response entity containing an API response wrapping the requested data.
   */
  @GetMapping("")
  public ResponseEntity<APIResponse<Object>> getMajorsFull(
    @RequestParam(value = "name", required = false) String name,
    @RequestParam(value = "majorCode", required = false) String majorCode,
    @RequestParam(value = "majorID", required = false) String majorID,
    @RequestParam(value = "schoolName", required = false) String schoolName,
    @RequestParam(value = "schoolID", required = false) String schoolID,
    @RequestParam(value = "semesterName", required = false) String semesterName,
    @RequestParam(value = "semesterID", required = false) String semesterID,
    @RequestParam(value = "sort", defaultValue = "name") String sort,
    @RequestParam(value = "order", defaultValue = "asc") String order,
    @RequestParam(value = "return", defaultValue = "id") String type
  ) {
    // Validate provided parameters against the existence in their respective services.
    if (majorID != null && !majors.existsById(majorID)) {
      throw new IllegalArgumentException(
        "A major with 'majorID' does not exist."
      );
    } else if (majorCode != null && !majors.existsByCode(majorCode)) {
      throw new IllegalArgumentException(
        "A major with 'majorCode' does not exist."
      );
    } else if (schoolID != null && !schools.existsById(schoolID)) {
      throw new IllegalArgumentException(
        "A school with 'schoolID' does not exist."
      );
    } else if (schoolName != null && !schools.existsByName(schoolName)) {
      throw new IllegalArgumentException(
        "A school with 'schoolName' does not exist."
      );
    } else if (semesterID != null && !semesters.existsById(semesterID)) {
      throw new IllegalArgumentException(
        "A semester with 'semesterID' does not exist."
      );
    } else if (semesterName != null && !semesters.existsByName(semesterName)) {
      throw new IllegalArgumentException(
        "A semester with 'semesterName' does not exist."
      );
    }

    // Retrieve semesterID by semesterName if semesterID was not provided.
    if (semesterID == null && semesterName != null) {
      semesterID = semesters.getByName(semesterName).getId();
    }

    // Retrieve schoolID by schoolName if schoolID was not provided.
    if (schoolID == null && schoolName != null) {
      schoolID = schools.getByName(schoolName).getId();
    }

    // Retrieve majorID by majorCode if majorID was not provided.
    if (majorID == null && majorCode != null) {
      majorID = majors.getByCode(majorCode).getId();
    }

    // Fetch a list of courses based on the provided filters.
    List<Course> result = courses.getCourses(
      majorID,
      semesterID,
      schoolID,
      name,
      sort,
      order
    );

    // Determine the response type and format the result accordingly.
    switch (type) {
      case "object":
        return ResponseEntity.ok(APIResponse.good(result));
      case "count":
        return ResponseEntity.ok(APIResponse.good(result.size()));
      default:
        List<String> ids = result.stream().map(Course::getId).toList();
        return ResponseEntity.ok(APIResponse.good(ids));
    }
  }

  /**
   * Retrieves a specific course by its unique identifier,
   * returning its details wrapped in an API response.
   *
   * @param id The unique identifier of the course to retrieve.
   * @return A response entity containing an API response wrapping the details of the specified course.
   */
  @GetMapping("/{id}")
  public ResponseEntity<APIResponse<Object>> getSpecificMajor(
    @PathVariable(value = "id") String id
  ) {
    // Fetch and return the course details using the provided course ID.
    return ResponseEntity.ok(APIResponse.good(courses.getById(id)));
  }
}
