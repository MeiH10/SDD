package Pucknotes.Server.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Note.Note;

import java.util.Date;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @DBRef
    @NonNull
    private Account account;

    @DBRef
    @NonNull
    private Note note;

    @Setter
    @NonNull
    private String description;

    @Setter
    private Date createdDate = new Date();
}