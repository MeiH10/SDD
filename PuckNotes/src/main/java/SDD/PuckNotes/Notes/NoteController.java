package SDD.PuckNotes.Notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    // Endpoint to add a new note
    @PostMapping("/add")
    public ResponseEntity<String> addNote(@RequestParam("title") String title,
                                          @RequestParam("file") MultipartFile file) {
        try {
            String noteId = noteService.addNote(title, file);
            return new ResponseEntity<>("Note created successfully with ID: " + noteId, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Error while uploading file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint to get a note by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable String id) {
        try {
            Note note = noteService.getNote(id);
            return new ResponseEntity<>(note, HttpStatus.OK);
        } catch (NoteNotFoundException e) {
            return new ResponseEntity<>("Note not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }
}