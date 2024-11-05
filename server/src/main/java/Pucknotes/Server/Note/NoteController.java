package Pucknotes.Server.Note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import Pucknotes.Server.Like.LikeService;
import Pucknotes.Server.Response.Types.UnauthorizedException;
import Pucknotes.Server.Session.SessionService;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private SessionService sessionService;

    private String getCurrentUserId(HttpServletRequest request) {
        return sessionService.getSession(request);
    }

    // Endpoint to add a new note
    @PostMapping("")
    public ResponseEntity<String> addNote(
            HttpServletRequest request,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) {
        try {
            String userID = getCurrentUserId(request);
            String noteId = noteService.addNote(title, file, userID);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(
            HttpServletRequest request,
            @PathVariable String id) {
        try {
            String userID = getCurrentUserId(request);
            noteService.deleteNote(id, userID);
            return new ResponseEntity<>("Note deleted successfully.", HttpStatus.OK);
        } catch (NoteNotFoundException e) {
            return new ResponseEntity<>("Note not found with ID: " + id, HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>("Unauthorized to delete the note: " + e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    // Endpoint to update an existing note
    @PutMapping("/{id}")
    public ResponseEntity<String> updateNote(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) MultipartFile file) {
        try {
            String userID = getCurrentUserId(request);
            noteService.updateNote(id, title, file, userID);
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

    // Endpoint to like a note
    @PostMapping("/{id}/like")
    public ResponseEntity<String> likeNote(
            HttpServletRequest request,
            @PathVariable String id) {
        try {
            String userID = getCurrentUserId(request);
            likeService.likeNote(id, userID);
            return new ResponseEntity<>("Note liked successfully.", HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    // Endpoint to check if the user has liked the note
    @GetMapping("/{id}/liked")
    public ResponseEntity<Boolean> hasLikedNote(
            HttpServletRequest request,
            @PathVariable String id) {
        String userID = getCurrentUserId(request);
        boolean hasLiked = likeService.hasLiked(id, userID);
        return new ResponseEntity<>(hasLiked, HttpStatus.OK);
    }

    // Endpoint to dislike a note (remove like)
    @DeleteMapping("/{id}/like")
    public ResponseEntity<String> dislikeNote(
            HttpServletRequest request,
            @PathVariable String id) {
        try {
            String userID = getCurrentUserId(request);
            likeService.dislikeNote(id, userID);
            return new ResponseEntity<>("Note disliked successfully.", HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}