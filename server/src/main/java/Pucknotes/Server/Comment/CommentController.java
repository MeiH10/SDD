package Pucknotes.Server.Comment;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Note.Note;
import Pucknotes.Server.Note.NoteService;
import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Session.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  /**
   * Creates a new comment for a specific note.
   *
   * @param request  The HTTP request object containing session data.
   * @param body     The text body of the comment to be created.
   * @param noteID   The ID of the note on which the comment will be made.
   * @return A ResponseEntity containing the API response with the comment ID.
   */
  @PostMapping("")
  public ResponseEntity<APIResponse<String>> createComment(
    HttpServletRequest request,
    @RequestParam(value = "body") String body,
    @RequestParam(value = "noteID") String noteID
  ) {
    // Retrieve the current user from the session.
    Account user = sessions.getCurrentUser(request);
    // Retrieve the note by its ID to ensure it exists.
    Note note = notes.getById(noteID);

    // Create the comment tied to the specified note and user.
    Comment comment = comments.createComment(note, user, body);
    return ResponseEntity.ok(APIResponse.good(comment.getId()));
  }

  /**
   * Retrieves comments based on specified criteria such as note ID or user ID.
   *
   * @param request  The HTTP request object containing session data.
   * @param noteID   Optional. The ID of the note for which comments are retrieved.
   * @param userID   Optional. The ID of the user whose comments are retrieved.
   * @param sort     The criteria for sorting comments, defaults to "likes".
   * @param order    The order of sorting, defaults to "asc".
   * @param type     The type of response desired; can be "object", "count", or "id".
   * @return A ResponseEntity containing the API response with requested comments data.
   */
  @GetMapping("")
  public ResponseEntity<APIResponse<?>> getComments(
    HttpServletRequest request,
    @RequestParam(value = "noteID", required = false) String noteID,
    @RequestParam(value = "ownerID", required = false) String userID,
    @RequestParam(value = "sort", defaultValue = "likes") String sort,
    @RequestParam(value = "order", defaultValue = "asc") String order,
    @RequestParam(value = "return", defaultValue = "id") String type
  ) {
    // Check if the provided noteID exists; if not, throw an exception.
    if (noteID != null && !notes.existsById(noteID)) {
      throw new ResourceNotFoundException("Note with 'noteID' does not exist.");
    }
    // Check if the provided userID exists; if not, throw an exception.
    else if (userID != null && !accounts.existsById(userID)) {
      throw new ResourceNotFoundException(
        "Account with 'userID' does not exist."
      );
    }

    // Retrieve the comments based on the specified criteria.
    List<Comment> result = comments.getComments(noteID, userID, sort, order);

    // Determine response type and format accordingly.
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

  /**
   * Retrieves a specific comment by its ID.
   *
   * @param id The ID of the comment to retrieve.
   * @return A ResponseEntity containing the API response with the specified comment.
   */
  @GetMapping("/{id}")
  public ResponseEntity<APIResponse<Comment>> getCommentById(
    @PathVariable String id
  ) {
    // Attempt to retrieve the comment by ID.
    Comment comment = comments.getById(id);
    return ResponseEntity.ok(APIResponse.good(comment));
  }

  /**
   * Edits an existing comment based on the provided ID and new body content.
   *
   * @param request  The HTTP request object containing session data.
   * @param id       The ID of the comment to be edited.
   * @param body     The new body content of the comment.
   * @return A ResponseEntity containing the API response with the updated comment.
   */
  @PutMapping("/{id}")
  public ResponseEntity<APIResponse<Comment>> editComment(
    HttpServletRequest request,
    @PathVariable String id,
    @RequestBody String body
  ) {
    // Retrieve the current user from the session.
    Account user = sessions.getCurrentUser(request);

    // Edit the comment with the given ID using the new body content.
    Comment updated = comments.editComment(id, body, user);
    return ResponseEntity.ok(APIResponse.good(updated));
  }

  /**
   * Deletes a specific comment by its ID.
   *
   * @param request  The HTTP request object containing session data.
   * @param id       The ID of the comment to be deleted.
   * @return A ResponseEntity containing the API response indicating success or failure.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<APIResponse<Boolean>> deleteComment(
    HttpServletRequest request,
    @PathVariable String id
  ) {
    // Retrieve the current user from the session.
    Account user = sessions.getCurrentUser(request);
    // Retrieve the comment to be deleted by its ID.
    Comment comment = comments.getById(id);

    // Perform the deletion of the comment.
    comments.deleteComment(comment, user);
    return ResponseEntity.ok(APIResponse.good(true));
  }

  /**
   * Likes a specific comment.
   *
   * @param request  The HTTP request object containing session data.
   * @param id       The ID of the comment to be liked.
   * @return A ResponseEntity containing the API response indicating success.
   */
  @PutMapping("/{id}/like")
  public ResponseEntity<APIResponse<Boolean>> likeNote(
    HttpServletRequest request,
    @PathVariable String id
  ) {
    // Retrieve the current user from the session.
    Account user = sessions.getCurrentUser(request);
    // Retrieve the comment that is to be liked.
    Comment comment = comments.getById(id);

    // Perform the liking of the comment.
    comments.like(user, comment);
    return ResponseEntity.ok(APIResponse.good(true));
  }

  /**
   * Checks if the current user has liked a specific comment.
   *
   * @param request  The HTTP request object containing session data.
   * @param id       The ID of the comment to be checked.
   * @return A ResponseEntity containing the API response with the like status.
   */
  @GetMapping("/{id}/like")
  public ResponseEntity<APIResponse<Boolean>> hasLikedNote(
    HttpServletRequest request,
    @PathVariable String id
  ) {
    // Retrieve the current user from the session.
    Account user = sessions.getCurrentUser(request);
    // Retrieve the comment to check the like status.
    Comment comment = comments.getById(id);

    // Return whether the user has liked the comment.
    return ResponseEntity.ok(
      APIResponse.good(comments.hasLiked(user, comment))
    );
  }

  /**
   * Dislikes a specific comment.
   *
   * @param request  The HTTP request object containing session data.
   * @param id       The ID of the comment to be disliked.
   * @return A ResponseEntity containing the API response indicating success.
   */
  @DeleteMapping("/{id}/like")
  public ResponseEntity<APIResponse<Boolean>> dislikeNote(
    HttpServletRequest request,
    @PathVariable String id
  ) {
    // Retrieve the current user from the session.
    Account user = sessions.getCurrentUser(request);
    // Retrieve the comment that is to be disliked.
    Comment comment = comments.getById(id);

    // Perform the disliking of the comment.
    comments.dislike(user, comment);

    return ResponseEntity.ok(APIResponse.good(false));
  }
}
