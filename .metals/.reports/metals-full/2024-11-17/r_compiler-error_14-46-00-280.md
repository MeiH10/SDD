file://<WORKSPACE>/server/src/main/java/Pucknotes/Server/Session/SessionService.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 849
uri: file://<WORKSPACE>/server/src/main/java/Pucknotes/Server/Session/SessionService.java
text:
```scala
package Pucknotes.Server.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class SessionService {
    @Autowired
    private AccountService accountService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String createSession(HttpServletRequest request, String email, String password) {
        Account account = accountService.getByEmail(email);
        
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new IllegalArgumentException("Password and email do not match.");
        @@}

        request.getSession().setAttribute("id", account.getId());
        return account.getId();
    }

    public void deleteSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public String getSession(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("id");
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