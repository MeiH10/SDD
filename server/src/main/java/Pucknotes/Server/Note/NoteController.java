package Pucknotes.Server.Note;

import org.apache.logging.log4j.util.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.File.File;
import Pucknotes.Server.File.FileService;
import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Section.Section;
import Pucknotes.Server.Section.SectionService;
import Pucknotes.Server.Session.SessionService;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/note")
public class NoteController {

    @Autowired
    private NoteService notes;

    @Autowired
    private SectionService sections;

    @Autowired
    private SessionService sessions;

    @Autowired
    private FileService files;

    // Endpoint to add a new note
    @PostMapping("")
    public ResponseEntity<APIResponse<String>> addNote(
            HttpServletRequest request,
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "description", defaultValue = "") String description,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "sectionID", required = true) String sectionID,
            @RequestParam(value = "tags", defaultValue = "") List<String> tags) {

        if (!sections.existsById(sectionID)) {
            throw new IllegalArgumentException("A section with 'sectionID' does not exist.");
        }

        Account user = sessions.getCurrentUser(request);
        Section section = sections.getById(sectionID);

        try {
            Note note = notes.createNote(user, section, title, description, file, link, tags);
            return ResponseEntity.ok(APIResponse.good(note.getId()));
        } catch (Exception error) {
            throw new InternalException("Could not create note.");
        }    
    }

    // // Endpoint to get all note IDs
    // @GetMapping("")
    // public ResponseEntity<List<String>> getAllNoteIds() {
    //     List<String> noteIds = notes.getAllNoteIds();
    // }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable String id) {
        Note note = notes.getById(id);
        return ResponseEntity.ok(APIResponse.good(note));
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<?> getNoteFileById(@PathVariable String id) throws IOException {

        Note note = notes.getById(id);
        File file = files.downloadFile(note.getFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(file.getFileType());
        headers.setContentLength(file.getFileSize());

        return new ResponseEntity<byte[]>(file.getFile(), headers, HttpStatus.OK);
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> deleteNote(
    //         HttpServletRequest request,
    //         @PathVariable String id) {
    // }

    // // Endpoint to update an existing note
    // @PutMapping("/{id}")
    // public ResponseEntity<String> updateNote(
    //         HttpServletRequest request,
    //         @PathVariable String id,
    //         @RequestParam(required = false) String title,
    //         @RequestParam(required = false) MultipartFile file) {
    // }

    // // Endpoint to like a note
    // @PostMapping("/{id}/like")
    // public ResponseEntity<String> likeNote(
    //         HttpServletRequest request,
    //         @PathVariable String id) {
    // }

    // // Endpoint to check if the user has liked the note
    // @GetMapping("/{id}/liked")
    // public ResponseEntity<Boolean> hasLikedNote(
    //         HttpServletRequest request,
    //         @PathVariable String id) {
    // }

    // // Endpoint to dislike a note (remove like)
    // @DeleteMapping("/{id}/like")
    // public ResponseEntity<String> dislikeNote(
    //         HttpServletRequest request,
    //         @PathVariable String id) {

    // }
}