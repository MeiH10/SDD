package Pucknotes.Server.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Session.SessionService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private SessionService sessionService;

    private String getCurrentUserId(HttpServletRequest request) {
        return sessionService.getSession(request);
    }

    @PostMapping("")
    public ResponseEntity<APIResponse<Comment>> createComment(
            HttpServletRequest request,
            @RequestBody String body,
            @RequestBody String noteID) {
        String currentUserId = getCurrentUserId(request);



        Comment createdComment = commentService.createComment(currentUserId, noteID, body);
        return ResponseEntity.ok(APIResponse.good(createdComment));
    }

    @GetMapping("/note/{noteId}")
    public ResponseEntity<APIResponse<List<Comment>>> getCommentsByNoteId(@PathVariable String noteId) {
        List<Comment> comments = commentService.getCommentsByNoteId(noteId);
        return ResponseEntity.ok(APIResponse.good(comments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable String id) {
        Optional<Comment> commentOpt = commentService.getCommentById(id);
        return commentOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Comment>> editComment(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestBody String body) {
        String currentUserId = getCurrentUserId(request);
        Comment updated = commentService.editComment(id, body, currentUserId);
        return ResponseEntity.ok(APIResponse.good(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteComment(
            HttpServletRequest request,
            @PathVariable String id) {
        String currentUserId = getCurrentUserId(request);
        commentService.deleteComment(id, currentUserId);
        return ResponseEntity.ok(APIResponse.good(true));
    }
}
