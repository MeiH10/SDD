package Pucknotes.Server.Verification;

import java.util.UUID;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import Pucknotes.Server.Account.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "verifications")
public class Verification {
    @Id
    private String id;

    @Setter
    @NonNull
    private Account details;

    @Setter
    private String token = UUID.randomUUID().toString();
}
