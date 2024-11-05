package Pucknotes.Server.Course;


import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Major.Major;
import Pucknotes.Server.Major.MajorService;
import Pucknotes.Server.Response.APIResponse;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    @Autowired
    private CourseService service;

    @Autowired
    private MajorService majors;

    @GetMapping("")
    public ResponseEntity<APIResponse<List<String>>> getCourses(
            @RequestParam(value = "major", defaultValue = "") String majorID,
            @RequestParam(value = "code", defaultValue = "") String code,
            @RequestParam(value = "majorCode", defaultValue = "") String majorCode,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order) {

        System.out.println(List.of(majorID, code, majorCode, name, sort, order));
        Major major = majorID.isEmpty()
            ? null
            : majors.getById(majorID);

        System.out.println(1);
        List<Course> courses = service.getCourses(major, majorCode, code, name, sort, order);
        System.out.println(2);
        Stream<Course> stream = courses.stream();
        System.out.println(3);
        List<String> results = stream.map(Course::getId).toList();
        System.out.println(4);
        return ResponseEntity.ok(APIResponse.good(results));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Course>> getCourseById(@PathVariable("id") String id) {
        Course semester = service.getById(id);
        return ResponseEntity.ok(APIResponse.good(semester));
    }
}