package Pucknotes.Server.Report;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Document(collection = "reports")
public class Report {
    @Id
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    @NonNull
    private String owner;

    @Field(targetType = FieldType.OBJECT_ID)
    @NonNull
    private String note;

    @Setter
    @NonNull
    private String title;

    @Setter
    @NonNull
    private String description;
}