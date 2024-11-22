package Pucknotes.Server.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Like.LikeService;
import Pucknotes.Server.Note.Note;
import Pucknotes.Server.Note.NoteService;
import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Session.SessionService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService comments;

    @Autowired
    private SessionService sessions;

    @Autowired
    private NoteService notes;

    @Autowired
    private LikeService likes;

    @PostMapping("")
    public ResponseEntity<APIResponse<Comment>> createComment(
            HttpServletRequest request,
            @RequestBody String body,
            @RequestBody String noteID) {

        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(noteID);

        Comment createdComment = comments.createComment(note, user, body);
        return ResponseEntity.ok(APIResponse.good(createdComment));
    }

    @GetMapping("")
    public ResponseEntity<APIResponse<List<Comment>>> getCommentsByNoteId(
            @RequestParam(value = "noteID") String noteID) {

        List<Comment> results = comments.getCommentsByNoteId(noteID);
        return ResponseEntity.ok(APIResponse.good(results));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Comment>> getCommentById(
            @PathVariable String id) {

        Comment comment = comments.getCommentById(id);
        return ResponseEntity.ok(APIResponse.good(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Comment>> editComment(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestBody String body) {

        Account user = sessions.getCurrentUser(request);
        Comment updated = comments.editComment(id, body, user);
        return ResponseEntity.ok(APIResponse.good(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteComment(
            HttpServletRequest request,
            @PathVariable String id) {

        Account user = sessions.getCurrentUser(request);
        Comment comment = comments.getCommentById(id);

        comments.deleteComment(comment, user);
        return ResponseEntity.ok(APIResponse.good(true));
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> likeNote(
            HttpServletRequest request,
            @PathVariable String id) {

        Account user = sessions.getCurrentUser(request);
        Comment comment = comments.getById(id);
        likes.likeComment(user, comment);

        return ResponseEntity.ok(APIResponse.good(true));
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> hasLikedNote(
            HttpServletRequest request,
            @PathVariable String id) {
        
        Account user = sessions.getCurrentUser(request);
        Comment comment = comments.getById(id);

        return ResponseEntity.ok(APIResponse.good(likes.hasLikedComment(user, comment)));
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> dislikeNote(
            HttpServletRequest request,
            @PathVariable String id) {
        
        Account user = sessions.getCurrentUser(request);
        Comment comment = comments.getById(id);
        likes.dislikeComment(user, comment);

        return ResponseEntity.ok(APIResponse.good(false));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<APIResponse<Comment.Statistics>> getStaistics(
            HttpServletRequest request,
            @PathVariable String id) {
        
        Comment comment = comments.getById(id);
        var stats = new Comment.Statistics(likes.totalCommentLikes(comment));

        return ResponseEntity.ok(APIResponse.good(stats));
    }
}
