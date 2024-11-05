package Pucknotes.Server.Account;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "accounts")
public class Account {
    @Id
    private String id;

    @Setter
    @NonNull
    private String email;

    @Setter
    @NonNull
    private String username;

    @Setter
    @NonNull
    private String password;

    public Account(String email, String username, String password, String firstname, String lastname) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
