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

/**
 * Represents a comment in the application.
 * This class is mapped to the "comments" collection in the MongoDB database.
 */
@Getter
@AllArgsConstructor // Generates a constructor with all parameters
@RequiredArgsConstructor // Generates a constructor with required (non-null) parameters
@NoArgsConstructor // Generates a no-arguments constructor
@Document(collection = "comments") // Specifies the MongoDB collection to store comments
public class Comment {
    
    /**
     * Unique identifier for the comment.
     * This field is marked as the document ID in MongoDB.
     */
    @Id
    private String id;

    /**
     * Identifier for the account that made the comment.
     * This identifier is required and will be stored as an ObjectId in MongoDB.
     */
    @Field(targetType = FieldType.OBJECT_ID)
    @NonNull
    private String account;

    /**
     * Identifier for the note that the comment is related to.
     * This identifier is required and will be stored as an ObjectId in MongoDB.
     */
    @Field(targetType = FieldType.OBJECT_ID)
    @NonNull
    private String note;

    /**
     * The textual content of the comment.
     * This field is required, and it can be modified via the setter.
     */
    @Setter
    @NonNull
    private String description;

    /**
     * The date when the comment was created.
     * This field initializes to the current date when a new comment instance is created.
     * It can be modified via the setter.
     */
    @Setter
    private Date createdDate = new Date();

    /**
     * The total number of likes received by the comment.
     * This field can be modified via the setter.
     */
    @Setter
    private long totalLikes;

    /**
     * A list of likes represented by their account identifiers.
     * This field initializes as an empty list and can be modified via the setter.
     */
    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> likes = List.of();
}
