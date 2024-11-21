package Pucknotes.Server.Note;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notes")
public class Note {
    @Id
    private String id;

    @Setter
    private String title, description;

    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private String owner;

    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private String file;

    @Setter
    private String link;

    @Setter
    private List<String> tags;

    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private String section, course, major, school, semester;
}