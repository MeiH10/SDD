package Pucknotes.Server.Note;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Binary image;
}