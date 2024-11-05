package Pucknotes.Server.Course;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Response.APIResponse;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    @Autowired
    private CourseService service;

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Course>> getCourseById(@PathVariable("id") String id) {
        Course semester = service.getById(id);
        return ResponseEntity.ok(APIResponse.good(semester));
    }
}