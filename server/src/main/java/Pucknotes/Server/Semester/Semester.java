package Pucknotes.Server.Semester;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "semesters")
public class Semester {
    @Id
    private String id;

    private String season;
    private Number year; 

    private String name;
}
