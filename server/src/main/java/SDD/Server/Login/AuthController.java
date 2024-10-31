package SDD.Server.Login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, HttpSession session) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required.");
        }

        // Add the username to the session
        session.setAttribute("username", username);
        return ResponseEntity.ok("User " + username + " logged in.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        if (session.getAttribute("username") != null) {
            session.invalidate();
            return ResponseEntity.ok("Logged out successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No active session found.");
        }
    }

    @GetMapping("/session-info")
    public ResponseEntity<String> sessionInfo(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            return ResponseEntity.ok("Current User: " + username);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No active session found.");
        }
    }
}