package Pucknotes.Server.Major;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.School.SchoolService;
import Pucknotes.Server.Semester.SemesterService;

@RestController
@RequestMapping("/api/major")
public class MajorController {
    @Autowired
    private MajorService majors;

    @Autowired
    private SchoolService schools;

    @Autowired
    private SemesterService semesters;

    @GetMapping("")
    public ResponseEntity<APIResponse<Object>> getMajorsFull(
            @RequestParam(value = "schoolName", required = false) String schoolName,
            @RequestParam(value = "schoolID", required = false) String schoolID,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "semesterName", required = false) String semesterName,
            @RequestParam(value = "semesterID", required = false) String semesterID,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "full", defaultValue = "false") boolean full) {

        if (schoolID != null && !schools.existsById(schoolID)) {
            throw new IllegalArgumentException("A school with 'schoolID' does not exist.");
        } else if (schoolName != null && !schools.existsById(schoolName)) {
            throw new IllegalArgumentException("A school with 'schoolName' does not exist.");
        } else if (semesterID != null && !semesters.existsById(semesterID)) {
            throw new IllegalArgumentException("A school with 'semesterID' does not exist.");
        } else if (semesterName != null && !semesters.existsById(semesterName)) {
            throw new IllegalArgumentException("A school with 'semesterName' does not exist.");
        }

        if (semesterID == null && semesterName != null) {
            semesterID = semesters.getByName(semesterName).getId();
        }

        if (schoolID == null && schoolName != null) {
            schoolID = semesters.getByName(schoolName).getId();
        }

        List<Major> result = majors.getMajors(schoolID, name, semesterID, sort, order);

        if (full) {
            return ResponseEntity.ok(APIResponse.good(result));
        } else {
            List<String> ids = result.stream().map(Major::getId).toList();
            return ResponseEntity.ok(APIResponse.good(ids));
        }
    }
}