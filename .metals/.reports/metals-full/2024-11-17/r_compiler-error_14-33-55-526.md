file://<WORKSPACE>/server/src/main/java/Pucknotes/Server/Account/AccountController.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 809
uri: file://<WORKSPACE>/server/src/main/java/Pucknotes/Server/Account/AccountController.java
text:
```scala
package Pucknotes.Server.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Session.SessionService;
import Pucknotes.Server.Verif@@ication.VerificationService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService service;

    @Autowired
    private VerificationService verify_service;

    @Autowired
    private SessionService sessionService;

    private String getCurrentUserId(HttpServletRequest request) {
        return sessionService.getSession(request);
    }

    @PostMapping("")
    public ResponseEntity<APIResponse<String>> createAccount(
            @RequestParam("token") String token,
            @RequestParam("registration") String registration) {

        Account details = verify_service.verifyToken(registration, token);
        Account account = service.registerAccount(details);
        return ResponseEntity.ok(APIResponse.good(account.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Account>> getAccount(@PathVariable String id) {
        Account account = service.getById(id);
        return ResponseEntity.ok(APIResponse.good(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password) {
        
        String userID = getCurrentUserId(request);
        Account account = service.getByEmail(id);
        account.setUsername(username);
        account.setPassword(password);
        service.updateAccount(account, userID);

        return new ResponseEntity<>("Account updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteAccount(
        HttpServletRequest request,
        @PathVariable String id) {
        try {
            String userID = getCurrentUserId(request);
            Account account = service.getById(id);
            service.deleteAccount(account, userID);
            return ResponseEntity.ok(APIResponse.good(true));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(APIResponse.good(false));
        }
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