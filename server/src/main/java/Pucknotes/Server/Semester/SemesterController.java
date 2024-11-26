package Pucknotes.Server.Semester;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import Pucknotes.Server.Response.APIResponse;

/**
 * SemesterController is a RESTful controller that handles requests related 
 * to semesters within the application. It provides endpoints to retrieve 
 * a list of all semesters and to fetch a specific semester by its ID.
 */
@RestController
@RequestMapping("/api/semester")
public class SemesterController {
    
    @Autowired
    private SemesterService service;

    /**
     * Retrieves a list of all semesters based on specified filters.
     *
     * @param year The year to filter semesters by. Defaults to -1, which means no filtering by year.
     * @param season The season to filter semesters by. Defaults to "any", which means no filtering by season.
     * @param sort The sorting order for the semesters. Defaults to "oldest".
     * @return A ResponseEntity containing an APIResponse with a list of semester IDs.
     *         This method responds with a 200 OK status and the results if successful.
     */
    @GetMapping("/")
    public ResponseEntity<APIResponse<List<String>>> getAllSemesters(
            @RequestParam(value = "year", defaultValue = "-1") Integer year,
            @RequestParam(value = "season", defaultValue = "any") String season,
            @RequestParam(value = "sort", defaultValue = "oldest") String sort) {

        // Fetching the list of semesters from the service based on the provided parameters.
        List<Semester> semesters = service.getSemesters(year, season, sort);

        // Mapping semester objects to their IDs to create a list of IDs.
        List<String> results = semesters.stream().map(Semester::getId).toList();

        // Returning a successful response with the list of semester IDs.
        return ResponseEntity.ok(APIResponse.good(results));
    }

    /**
     * Retrieves a specific semester by its ID.
     *
     * @param id The ID of the semester to retrieve.
     * @return A ResponseEntity containing an APIResponse with the requested Semester object.
     *         This method responds with a 200 OK status if the semester is found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Semester>> getSemesterById(@PathVariable("id") String id) {
        
        // Fetching the semester by its ID using the service layer.
        Semester semester = service.getById(id);

        // Returning a successful response with the semester object.
        return ResponseEntity.ok(APIResponse.good(semester));
    }
}
