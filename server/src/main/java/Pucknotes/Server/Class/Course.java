package Pucknotes.Server.Class;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import Pucknotes.Server.Major.Major;
import Pucknotes.Server.Semester.Semester;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
public class Course {
    @Id
    private String id;

    @DBRef
    private Major major;

    @DBRef
    private Semester semester;
    
    private String code;
}
