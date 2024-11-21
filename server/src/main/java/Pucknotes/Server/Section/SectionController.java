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

@RestController
@RequestMapping("/api/section")
public class SectionController {
    @Autowired
    private SectionService sections;

    @Autowired
    private CourseService courses;

    @GetMapping("")
    public ResponseEntity<APIResponse<Object>> getSections(
            @RequestParam(value = "courseCode", required = false) String courseCode,
            @RequestParam(value = "courseID", required = false) String courseID,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "full", defaultValue = "false") boolean full) {
        
        if (courseID != null && !courses.existsById(courseID)) {
            throw new IllegalArgumentException("A course with 'courseID' does not exist.");
        } else if (courseCode != null && !courses.existsByCode(courseCode)) {
            throw new IllegalArgumentException("A course with 'courseCode' does not exist.");
        }

        if (courseID == null && courseCode != null) {
            courseID = courses.getByCode(courseCode).getId();
        }

        List<Section> result = sections.getSections(courseID, sort, order);

        if (full) {
            return ResponseEntity.ok(APIResponse.good(result));
        } else {
            List<String> ids = result.stream().map(Section::getId).toList();
            return ResponseEntity.ok(APIResponse.good(ids));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Section>> getSectionById(
            @PathVariable("id") String id) {

        Section semester = sections.getById(id);
        return ResponseEntity.ok(APIResponse.good(semester));
    }
}