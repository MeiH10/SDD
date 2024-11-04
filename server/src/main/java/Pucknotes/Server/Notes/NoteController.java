package Pucknotes.Server.Notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    // Endpoint to add a new note
    @PostMapping("")
    public ResponseEntity<String> addNote(
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) {
        try {
            String noteId = noteService.addNote(title, file);
            return new ResponseEntity<>("Note created successfully with ID: " + noteId, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Error while uploading file: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint to get all note IDs
    @GetMapping("")
    public ResponseEntity<List<String>> getAllNoteIds() {
        List<String> noteIds = noteService.getAllNoteIds();
        return new ResponseEntity<>(noteIds, HttpStatus.OK);
    }

    // Endpoint to get a note by ID
    // Endpoint to get a note by ID or download the file
    @GetMapping("/{id}")
    public ResponseEntity<?> getNoteById(
            @PathVariable String id,
            @RequestParam(required = false) String download) {
        try {
            Note note = noteService.getNote(id);
            if ("true".equalsIgnoreCase(download)) {
                // Prepare to download the file
                byte[] fileData = note.getImage().getData();
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + note.getTitle() + "\"");
                return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
            } else {
                // Return note information
                return new ResponseEntity<>(note, HttpStatus.OK);
            }
        } catch (NoteNotFoundException e) {
            return new ResponseEntity<>("Note not found with ID: " + id, HttpStatus.NOT_FOUND);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Note not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to update an existing note
    @PutMapping("/{id}")
    public ResponseEntity<String> updateNote(@PathVariable String id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) MultipartFile file) {
        try {
            noteService.updateNote(id, title, file);
            return new ResponseEntity<>("Note updated successfully.", HttpStatus.OK);
        } catch (NoteNotFoundException e) {
            return new ResponseEntity<>("Note not found with ID: " + id, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Error while uploading file: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}