file://<WORKSPACE>/server/src/main/java/Pucknotes/Server/Comment/CommentController.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 215
uri: file://<WORKSPACE>/server/src/main/java/Pucknotes/Server/Comment/CommentController.java
text:
```scala
package Pucknotes.Server.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Pucknotes.S@@erver.Response.APIResponse;
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

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.MetalsDriver.run(MetalsDriver.scala:45)
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:376)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator