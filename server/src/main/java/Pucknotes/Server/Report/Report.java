package Pucknotes.Server.Report;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Document(collection = "comments")
public class Report {
    @Id
    private String id;

    @Setter
    private String title;

    @Setter
    private String description;

    @Setter
    private String type;

    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private String item;
}
