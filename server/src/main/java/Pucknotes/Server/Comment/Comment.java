package Pucknotes.Server.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    @NonNull
    private String account;

    @Field(targetType = FieldType.OBJECT_ID)
    @NonNull
    private String note;

    @Setter
    @NonNull
    private String description;

    @Setter
    private Date createdDate = new Date();

    @Setter
    private long totalLikes;

    @Setter
    // @NonNull
    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> likes = List.of();
}