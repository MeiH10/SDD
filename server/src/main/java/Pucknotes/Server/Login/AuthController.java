package Pucknotes.Server.Login;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestParam String username, HttpSession session) {
        session.setAttribute("username", username);
        return "User " + username + " logged in.";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Logged out successfully.";
    }

    @GetMapping("/session-info")
    public String sessionInfo(HttpSession session) {
        return "Current User: " + session.getAttribute("username");
    }
}
