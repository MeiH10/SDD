package Pucknotes.Server.Major;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Response.APIResponse;

@RestController
@RequestMapping("/api/major")
public class MajorController {
    @Autowired
    private MajorService service;

    @GetMapping("")
    public ResponseEntity<APIResponse<Object>> getMajorsFull(
            @RequestParam(value = "school", required = false) String school,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "full", defaultValue = "false") boolean full) {

        List<Major> majors = service.getMajors(school, name, semester, sort, order);

        if (full) {
            return ResponseEntity.ok(APIResponse.good(majors));
        } else {
            List<String> ids = majors.stream().map(Major::getId).toList();
            return ResponseEntity.ok(APIResponse.good(ids));
        }
    }
}