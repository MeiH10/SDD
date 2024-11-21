package Pucknotes.Server.Like;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Note.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "likes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    @Id
    private String id;

    @DBRef
    private Account user;

    @DBRef
    private Note note;
}