package Pucknotes.Server.Section;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Response.APIResponse;

@RestController
@RequestMapping("/api/section")
public class SectionController {
    @Autowired
    private SectionService service;

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Section>> getSectionById(@PathVariable("id") String id) {
        Section semester = service.getById(id);
        return ResponseEntity.ok(APIResponse.good(semester));
    }
}