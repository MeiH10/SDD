package Pucknotes.Server.Section;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Course.CourseService;
import Pucknotes.Server.Response.APIResponse;

/**
 * The SectionController class handles HTTP requests related to sections.
 * It provides endpoints to retrieve sections, either as a list or by their unique identifier.
 */
@RestController
@RequestMapping("/api/section")
public class SectionController {
    
    @Autowired
    private SectionService sections;

    @Autowired
    private CourseService courses;

    /**
     * Retrieves a list of sections based on optional parameters.
     *
     * @param courseCode The code of the course to filter sections. This parameter is optional.
     * @param courseID The unique identifier of the course to filter sections. This parameter is optional.
     * @param sort The attribute by which to sort the sections. Defaults to "name".
     * @param order The sort order, can be "asc" or "desc". Defaults to "asc".
     * @param type The type of response expected: "object" for sections as objects, "count" for the number of sections, or "id" for just the section IDs. Defaults to "id".
     * @return A ResponseEntity containing an APIResponse object with the required data about sections.
     */
    @GetMapping("")
    public ResponseEntity<APIResponse<Object>> getSections(
            @RequestParam(value = "courseCode", required = false) String courseCode,
            @RequestParam(value = "courseID", required = false) String courseID,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "return", defaultValue = "id") String type) {
        
        // Check if courseID is valid using the CourseService.
        if (courseID != null && !courses.existsById(courseID)) {
            throw new IllegalArgumentException("A course with 'courseID' does not exist.");
        } 
        
        // Check if courseCode is valid using the CourseService.
        else if (courseCode != null && !courses.existsByCode(courseCode)) {
            throw new IllegalArgumentException("A course with 'courseCode' does not exist.");
        }

        // If only courseCode is provided, convert it to courseID.
        if (courseID == null && courseCode != null) {
            courseID = courses.getByCode(courseCode).getId();
        }

        // Retrieve sections based on courseID, sort order, and sort attribute.
        List<Section> result = sections.getSections(courseID, sort, order);

        // Prepare the response based on the requested type.
        switch (type) {
            case "object":
                return ResponseEntity.ok(APIResponse.good(result));
            case "count":
                return ResponseEntity.ok(APIResponse.good(result.size()));
            default:
                List<String> ids = result.stream().map(Section::getId).toList();
                return ResponseEntity.ok(APIResponse.good(ids));
        }
    }

    /**
     * Retrieves a single section by its unique identifier.
     *
     * @param id The unique identifier of the section to retrieve.
     * @return A ResponseEntity containing an APIResponse with the requested section data.
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Section>> getSectionById(
            @PathVariable("id") String id) {

        // Fetch the section using the SectionService by its ID.
        Section semester = sections.getById(id);

        // Return the section wrapped in a successful APIResponse.
        return ResponseEntity.ok(APIResponse.good(semester));
    }
}
