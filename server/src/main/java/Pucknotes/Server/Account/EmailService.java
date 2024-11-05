package Pucknotes.Server.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

// import lombok.AllArgsConstructor;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailer;

    public void sendVerify(String toEmail, String token) {
        String link = UriComponentsBuilder
                .fromUriString("http://localhost:8080/api/auth/verify-email")
                .queryParam("token", token)
                .toUriString();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Verification");
        message.setText("Verify your account by clicking on this link: " + link);

        // Optional debug log
        System.out.println("Sending email to: " + toEmail);
        System.out.println("Verification link: " + link);

        mailer.send(message);
    }
}
