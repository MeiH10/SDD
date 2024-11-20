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

@RestController
@RequestMapping("/api/school")
public class SchoolController {
    @Autowired
    private SchoolService schools;

    @Autowired
    private SemesterService semesters;

    @GetMapping("")
    public ResponseEntity<APIResponse<Object>> getSchoolsFull(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "semesterName", required = false) String semesterName,
            @RequestParam(value = "semesterID", required = false) String semesterID,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "full", defaultValue = "false") boolean full) {

        if (semesterID != null && !semesters.existsById(semesterID)) {
            throw new IllegalArgumentException("A semester with 'semesterID' does not exist.");
        } else if (semesterName != null && !semesters.existsByName(semesterName)) {
            throw new IllegalArgumentException("A semester with 'semesterName' does not exist.");
        }

        if (semesterID == null && semesterName != null) {
            semesterID = semesters.getByName(semesterName).getId();
        }

        List<School> result = schools.getSchool(name, semesterID, sort, order);

        if (full) {
            return ResponseEntity.ok(APIResponse.good(result));
        } else {
            List<String> ids = result.stream().map(School::getId).toList();
            return ResponseEntity.ok(APIResponse.good(ids));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> getSpecificSchool(
            @PathVariable(value = "id") String id) {

        return ResponseEntity.ok(APIResponse.good(schools.getById(id)));
    }
}