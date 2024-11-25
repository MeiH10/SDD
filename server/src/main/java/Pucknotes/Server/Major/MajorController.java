package Pucknotes.Server.Major;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.School.SchoolService;
import Pucknotes.Server.Semester.SemesterService;

/**
 * MajorController is a Spring REST controller that handles HTTP requests 
 * related to academic majors. It provides endpoints for retrieving 
 * a list of majors and details about specific majors based on various 
 * request parameters.
 */
@RestController
@RequestMapping("/api/major")
public class MajorController {
    @Autowired
    private MajorService majors;

    @Autowired
    private SchoolService schools;

    @Autowired
    private SemesterService semesters;

    /**
     * This endpoint retrieves a list of academic majors based on optional 
     * filter criteria such as name, school name or ID, semester name or 
     * ID, sorting options, and return type.
     *
     * @param name Optional name of the major to filter by.
     * @param schoolName Optional name of the school to filter by.
     * @param schoolID Optional ID of the school to filter by.
     * @param semesterName Optional name of the semester to filter by.
     * @param semesterID Optional ID of the semester to filter by.
     * @param sort Optional parameter to specify the sorting criteria. 
     *             Default is by name.
     * @param order Optional parameter to specify the sorting order. 
     *              Default is ascending.
     * @param type Optional parameter that indicates the return type. 
     *             Default is to return IDs.
     * @return A ResponseEntity containing an APIResponse object,
     *         which includes the filtered list of majors, count of majors, 
     *         or the IDs of the majors, as specified by the 'type' parameter.
     */
    @GetMapping("")
    public ResponseEntity<APIResponse<Object>> getMajorsFull(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "schoolName", required = false) String schoolName,
            @RequestParam(value = "schoolID", required = false) String schoolID,
            @RequestParam(value = "semesterName", required = false) String semesterName,
            @RequestParam(value = "semesterID", required = false) String semesterID,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "return", defaultValue = "id") String type) {

        // Validate the existence of the specified school or semester by their IDs or names.
        if (schoolID != null && !schools.existsById(schoolID)) {
            throw new IllegalArgumentException("A school with 'schoolID' does not exist.");
        } else if (schoolName != null && !schools.existsByName(schoolName)) {
            throw new IllegalArgumentException("A school with 'schoolName' does not exist.");
        } else if (semesterID != null && !semesters.existsById(semesterID)) {
            throw new IllegalArgumentException("A semester with 'semesterID' does not exist.");
        } else if (semesterName != null && !semesters.existsByName(semesterName)) {
            throw new IllegalArgumentException("A semester with 'semesterName' does not exist.");
        }

        // If the semester ID is not provided, but the semester name is, retrieve the ID based on the name.
        if (semesterID == null && semesterName != null) {
            semesterID = semesters.getByName(semesterName).getId();
        }

        // Similarly, if the school ID is not provided but the school name is, retrieve the ID based on the name.
        if (schoolID == null && schoolName != null) {
            schoolID = schools.getByName(schoolName).getId();
        }

        // Retrieve a list of majors based on the given filters.
        List<Major> result = majors.getMajors(semesterID, schoolID, name, sort, order);

        // Determine the response type and prepare the API response accordingly.
        switch (type) {
            case "object":
                return ResponseEntity.ok(APIResponse.good(result));
            case "count":
                return ResponseEntity.ok(APIResponse.good(result.size()));
            default:
                List<String> ids = result.stream().map(Major::getId).toList();
                return ResponseEntity.ok(APIResponse.good(ids));
        }
    }

    /**
     * This endpoint retrieves details about a specific major identified 
     * by its ID.
     *
     * @param id The ID of the major to be retrieved.
     * @return A ResponseEntity containing an APIResponse object that 
     *         includes the details of the specified major.
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> getSpecificMajor(
            @PathVariable(value = "id") String id) {

        // The call to the major service effectively retrieves the details 
        // of the specified major using its ID, which is then wrapped 
        // in an API response.
        return ResponseEntity.ok(APIResponse.good(majors.getById(id)));
    }
}
