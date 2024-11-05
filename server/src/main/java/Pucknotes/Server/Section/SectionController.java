package Pucknotes.Server.Section;


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
@RequestMapping("/api/section")
public class SectionController {
    @Autowired
    private SectionService service;

    @GetMapping("/")
    public List<Section> getSections(
        @RequestParam(value = "course", required = false) String courseId,
        @RequestParam(value = "number", required = false) String number
    ) {
        return service.getSections(courseId, number);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Section>> getSectionById(@PathVariable("id") String id) {
        Section semester = service.getById(id);
        return ResponseEntity.ok(APIResponse.good(semester));
    }
}