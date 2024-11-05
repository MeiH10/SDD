package Pucknotes.Server.Note;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Course.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.bson.types.Binary;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    @Getter @Setter
    private String id;

    @Getter @Setter
    @NonNull
    private String title;

    @Getter @Setter
    @NonNull
    @DBRef
    private Course course;

    @Getter
    @NonNull
    @DBRef
    private Account owner;

    @Getter @Setter
    @NonNull
    private Binary image;
}