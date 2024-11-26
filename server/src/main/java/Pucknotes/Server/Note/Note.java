package Pucknotes.Server.Note;

import java.util.List;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Note class represents a note entity in the system.
 * It is annotated as a MongoDB document and mapped to the "notes" collection.
 * Each Note object contains various attributes such as title, description,
 * and associated metadata such as owner and associated tags. 
 * This class uses Lombok annotations for automatic generation 
 * of getter, setter, and constructor methods.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notes")
public class Note {
    
    /**
     * Unique identifier for the note.
     * This field is automatically generated and assigned by MongoDB.
     */
    @Id
    private String id;

    /**
     * Title of the note.
     * This field can be modified after creation.
     */
    @Setter
    private String title;

    /**
     * Description of the note.
     * This provides additional context about the note.
     * This field can also be modified after creation.
     */
    @Setter
    private String description;

    /**
     * The owner of the note, identified by their unique ID.
     * This field is intended to trace ownership and can be modified.
     */
    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private String owner;

    /**
     * If the note is associated with a file, 
     * this field holds the file's unique ID.
     * This field can be modified as needed.
     */
    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private String file;

    /**
     * A link related to the note.
     * This can point to resources or references pertinent to the note.
     * This field can also be modified after creation.
     */
    @Setter
    private String link;

    /**
     * A list of tags associated with the note.
     * Tags help in categorizing and searching for notes.
     * This field can be modified by the user.
     */
    @Setter
    private List<String> tags;

    /**
     * The section identifier the note belongs to.
     * This helps in organizing notes within specific subjects or topics.
     * This field can also be modified.
     */
    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private String section;

    /**
     * The course identifier associated with the note.
     * This provides context regarding the academic course.
     * This field can be modified as needed.
     */
    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private String course;

    /**
     * The major identifier related to the note.
     * This is primarily used for educational context and organization.
     * This field can also be modified.
     */
    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private String major;

    /**
     * The school identifier associated with the note.
     * This gives information about the educational institution.
     * This field can be modified if necessary.
     */
    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private String school;

    /**
     * The semester identifier relevant to the note.
     * This helps in tracking when the note was created or relevant.
     * This field can be modified as needed.
     */
    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private String semester;

    /**
     * The date when the note was created.
     * This is automatically initialized to the current date and time when
     * a new Note instance is created.
     * This field can also be modified if the creation date needs to be updated.
     */
    @Setter
    private Date createdDate = new Date();

    /**
     * The total number of likes received by this note.
     * This field is modifiable and helps in understanding the note's popularity.
     */
    @Setter
    private long totalLikes;

    /**
     * A list of user IDs who have liked the note.
     * This utilizes a list to accommodate multiple likes and can be modified.
     */
    @Setter
    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> likes = List.of();

    /**
     * A flag indicating whether the note is anonymous.
     * If true, it indicates that the author's identity is concealed.
     * This field can be modified by the owner of the note.
     */
    @Setter
    private boolean anonymous;
}
