package Pucknotes.Server.Session;

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
