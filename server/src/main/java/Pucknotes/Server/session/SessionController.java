package Pucknotes.Server.session;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/session")
public class SessionController {

    @PostMapping("")
    public String login(@RequestParam String username, HttpSession session) {
        session.setAttribute("username", username);
        return "User " + username + " logged in.";
    }

    @DeleteMapping("")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Logged out successfully.";
    }

    @GetMapping("/")
    public String sessionInfo(HttpSession session) {
        return "Current User: " + session.getAttribute("username");
    }
}
