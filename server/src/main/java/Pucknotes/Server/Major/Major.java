package Pucknotes.Server.Major;

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
@Document(collection = "majors")
public class Major {
    @Id
    private String id;

    private String code;
    private String name;

    @Field(targetType = FieldType.OBJECT_ID)
    private String school;

    @Field(targetType = FieldType.OBJECT_ID)
    private String semester;
}
