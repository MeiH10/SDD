package Pucknotes.Server.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SessionService {
    @Autowired
    private AccountService accountService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String createSession(HttpServletRequest request, String email, String password) {
        Account account = accountService.getByEmail(email);
        
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new IllegalArgumentException("Password and email do not match.");
        }

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
