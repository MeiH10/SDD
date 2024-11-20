package Pucknotes.Server.School;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import Pucknotes.Server.Semester.Semester;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "schools")
public class School {
    @Id
    private String id;

    private String name;

    @DBRef
    private Semester semester;
}
