package Pucknotes.Server.Major;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Semester.Semester;
import Pucknotes.Server.Semester.SemesterService;

@RestController
@RequestMapping("/api/major")
public class MajorController {
    @Autowired
    private MajorService service;

    @Autowired
    private SemesterService semesters;

    @GetMapping("")
    public ResponseEntity<APIResponse<List<String>>> getMajors(
            @RequestParam(value = "school", defaultValue = "") String school,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "semester", defaultValue = "") String semesterId,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order) {

        Semester semester = semesterId.isEmpty()
            ? null
            : semesters.getById(semesterId);

        List<Major> majors = service.getMajors(school, name, semester, sort, order);
        List<String> results = majors.stream().map(Major::getId).toList();
        return ResponseEntity.ok(APIResponse.good(results));
    }
}