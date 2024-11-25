package Pucknotes.Server.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Note.Note;
import Pucknotes.Server.Note.NoteService;
import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
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
    private AccountService accounts;

    @Autowired
    private NoteService notes;

    @PostMapping("")
    public ResponseEntity<APIResponse<String>> createComment(
            HttpServletRequest request,
            @RequestParam(value = "body") String body,
            @RequestParam(value = "noteID") String noteID) {

        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(noteID);

        Comment comment = comments.createComment(note, user, body);
        return ResponseEntity.ok(APIResponse.good(comment.getId()));
    }

    @GetMapping("")
    public ResponseEntity<APIResponse<?>> getComments(
            HttpServletRequest request,
            @RequestParam(value = "noteID", required = false) String noteID,
            @RequestParam(value = "ownerID", required = false) String userID,
            @RequestParam(value = "sort", defaultValue = "likes") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "return", defaultValue = "id") String type) {
        
        if (noteID != null && !notes.existsById(noteID)) {
            throw new ResourceNotFoundException("Note with 'noteID' does not exist.");
        } else if (userID != null && !accounts.existsById(userID)) {
            throw new ResourceNotFoundException("Account with 'userID' does not exist.");
        }

        List<Comment> result = comments.getComments(noteID, userID, sort, order);
        switch (type) {
            case "object":
                return ResponseEntity.ok(APIResponse.good(result));
            case "count":
                return ResponseEntity.ok(APIResponse.good(result.size()));
            default:
                List<String> ids = result.stream().map(Comment::getId).toList();
                return ResponseEntity.ok(APIResponse.good(ids));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Comment>> getCommentById(
            @PathVariable String id) {

        Comment comment = comments.getById(id);
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
        Comment comment = comments.getById(id);

        comments.deleteComment(comment, user);
        return ResponseEntity.ok(APIResponse.good(true));
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> likeNote(
            HttpServletRequest request,
            @PathVariable String id) {

        Account user = sessions.getCurrentUser(request);
        Comment comment = comments.getById(id);
        comments.like(user, comment);

        return ResponseEntity.ok(APIResponse.good(true));
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> hasLikedNote(
            HttpServletRequest request,
            @PathVariable String id) {
        
        Account user = sessions.getCurrentUser(request);
        Comment comment = comments.getById(id);

        return ResponseEntity.ok(APIResponse.good(comments.hasLiked(user, comment)));
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> dislikeNote(
            HttpServletRequest request,
            @PathVariable String id) {
        
        Account user = sessions.getCurrentUser(request);
        Comment comment = comments.getById(id);
        comments.dislike(user, comment);

        return ResponseEntity.ok(APIResponse.good(false));
    }
}
