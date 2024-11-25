package Pucknotes.Server.Report;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reports")
public class Report {
    @Id
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    @NonNull
    private String owner;

    private String type;

    @Field(targetType = FieldType.OBJECT_ID)
    private String item;

    @Setter
    @NonNull
    private String title;

    @Setter
    @NonNull
    private String description;

    private Date createdDate = new Date();
}