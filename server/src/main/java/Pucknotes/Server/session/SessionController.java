package Pucknotes.Server.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    @Autowired
    private AccountService accounts;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("")
    public ResponseEntity<APIResponse<String>> createSession(
            HttpServletRequest request,
            @RequestParam String email,
            @RequestParam String password) {

        Account account = accounts.getByEmail(email);
        if (!encoder.matches(password, account.getPassword())) {
            throw new IllegalArgumentException("Password and email do not match.");
        }

        request.getSession().setAttribute("id", account.getId());
        return ResponseEntity.ok(APIResponse.good(account.getId()));
    }

    @DeleteMapping("")
    public ResponseEntity<APIResponse<Boolean>> deleteSession(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok(APIResponse.good(false));
    }

    @GetMapping("")
    public ResponseEntity<APIResponse<String>> getSession(HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute("id");
        return ResponseEntity.ok(APIResponse.good(id));
    }
}
