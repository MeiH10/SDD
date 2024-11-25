package Pucknotes.Server.School;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Semester.SemesterService;

/**
 * This class is a controller for handling school-related requests.
 * It provides endpoints to retrieve information about schools, including options for filtering and sorting.
 */
@RestController
@RequestMapping("/api/school")
public class SchoolController {
    
    @Autowired
    private SchoolService schools;

    @Autowired
    private SemesterService semesters;

    /**
     * Retrieves a list of schools based on optional query parameters such as
     * name, semester name, semester ID, sort order, sort type, and return type.
     * 
     * @param name          An optional parameter to filter schools by their name.
     * @param semesterName  An optional parameter to filter schools by the semester name.
     * @param semesterID    An optional parameter to filter schools by semester ID.
     * @param sort          A parameter determining the sorting field, defaults to "name".
     * @param order         A parameter determining the sorting order, defaults to "asc".
     * @param type          An optional parameter that defines the return type,
     *                      which can be "object", "count", or "id".
     * @return             A ResponseEntity containing an APIResponse wrapping
     *                     the result of the schools query, formatted per specified type.
     * @throws IllegalArgumentException if semesterID or semesterName is invalid.
     */
    @GetMapping("")
    public ResponseEntity<APIResponse<Object>> getSchoolsFull(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "semesterName", required = false) String semesterName,
            @RequestParam(value = "semesterID", required = false) String semesterID,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "return", defaultValue = "id") String type) {
        
        // Validate the provided semesterID against existing semesters.
        if (semesterID != null && !semesters.existsById(semesterID)) {
            throw new IllegalArgumentException("A semester with 'semesterID' does not exist.");
        } else if (semesterName != null && !semesters.existsByName(semesterName)) {
            throw new IllegalArgumentException("A semester with 'semesterName' does not exist.");
        }

        // If semesterID is not provided but semesterName is, retrieve the semesterID using semesterName.
        if (semesterID == null && semesterName != null) {
            semesterID = semesters.getByName(semesterName).getId();
        }

        // Fetch the list of schools based on the provided filters and sorting options.
        List<School> result = schools.getSchool(name, semesterID, sort, order);

        // Return the response based on the requested type.
        switch (type) {
            case "object":
                // Return the full objects.
                return ResponseEntity.ok(APIResponse.good(result));
            case "count":
                // Return the count of the retrieved school entities.
                return ResponseEntity.ok(APIResponse.good(result.size()));
            default:
                // Return only the IDs of the retrieved schools.
                List<String> ids = result.stream().map(School::getId).toList();
                return ResponseEntity.ok(APIResponse.good(ids));
        }
    }

    /**
     * Retrieves a specific school by its unique identifier.
     *
     * @param id The unique identifier of the school to be retrieved.
     * @return A ResponseEntity containing an APIResponse wrapping the school entity.
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> getSpecificSchool(
            @PathVariable(value = "id") String id) {

        // Directly retrieve and return the school entity by its ID.
        return ResponseEntity.ok(APIResponse.good(schools.getById(id)));
    }
}
