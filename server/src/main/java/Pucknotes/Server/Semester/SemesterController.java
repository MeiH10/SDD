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

@RestController
@RequestMapping("/api/semester")
public class SemesterController {
    @Autowired
    private SemesterService service;

    @GetMapping("/")
    public ResponseEntity<APIResponse<List<String>>> getAllSemesters(
            @RequestParam(value = "year", defaultValue = "-1") Integer year,
            @RequestParam(value = "season", defaultValue = "any") String season,
            @RequestParam(value = "sort", defaultValue = "oldest") String sort) {

        List<Semester> semesters = service.getSemesters(year, season, sort);
        List<String> results = semesters.stream().map(Semester::getId).toList();
        return ResponseEntity.ok(APIResponse.good(results));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Semester>> getSemesterById(@PathVariable("id") String id) {
        Semester semester = service.getById(id);
        return ResponseEntity.ok(APIResponse.good(semester));
    }
}