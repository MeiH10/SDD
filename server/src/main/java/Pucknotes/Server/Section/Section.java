package Pucknotes.Server.Section;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

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

    @Field(targetType = FieldType.OBJECT_ID)
    private String course, major, school, semester;

    private List<String> professors;
    private String number;
}
