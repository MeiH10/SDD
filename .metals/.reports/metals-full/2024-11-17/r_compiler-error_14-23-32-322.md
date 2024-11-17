file://<WORKSPACE>/server/src/main/java/Pucknotes/Server/Session/SessionController.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 10
uri: file://<WORKSPACE>/server/src/main/java/Pucknotes/Server/Session/SessionController.java
text:
```scala
package Pu@@cknotes.Server.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Pucknotes.Server.Response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @PostMapping("")
    public ResponseEntity<APIResponse<String>> createSession(
            HttpServletRequest request,
            @RequestParam String email,
            @RequestParam String password) {
        
        String accountId = sessionService.createSession(request, email, password);
        return ResponseEntity.ok(APIResponse.good(accountId));
    }

    @DeleteMapping("")
    public ResponseEntity<APIResponse<Boolean>> deleteSession(HttpServletRequest request) {
        sessionService.deleteSession(request);
        return ResponseEntity.ok(APIResponse.good(false));
    }

    @GetMapping("")
    public ResponseEntity<APIResponse<String>> getSession(HttpServletRequest request) {
        String accountId = sessionService.getSession(request);
        return ResponseEntity.ok(APIResponse.good(accountId));
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