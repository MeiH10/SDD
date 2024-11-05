package Pucknotes.Server.Major;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "majors")
public class Major {
    @Id
    private String id;

    private String department;
    private String code;
    private String name;
}
