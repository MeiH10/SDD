package Pucknotes.Server.Section;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import Pucknotes.Server.Class.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sections")
public class Section {
    @Id
    private String id;

    @DBRef
    private Course  course;

    private List<String> professors;
    private Number section;
}
