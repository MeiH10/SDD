package Pucknotes.Server.Course;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The Course class represents a course entity in the system.
 * It is annotated as a document to be stored in a MongoDB collection named "courses".
 * This class contains the details of a course, including its identifier, major, school,
 * semester, course code, and course name.
 * 
 * The Course class uses Lombok annotations to automatically generate the boilerplate code for 
 * getters and constructors, thereby improving code readability and reducing clutter.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
public class Course {
    
    /** 
     * The unique identifier for the course.
     * This field is automatically generated by the database 
     * when a new course is created. 
     */
    @Id
    private String id;

    /** 
     * The major that the course is associated with.
     * This field utilizes the ObjectId type, which is commonly used for references to other documents.
     */
    @Field(targetType = FieldType.OBJECT_ID)
    private String major;

    /** 
     * The school associated with the course.
     * This field also utilizes the ObjectId type for database references.
     */
    @Field(targetType = FieldType.OBJECT_ID)
    private String school;

    /** 
     * The semester during which the course is offered.
     * This field is a simple string that provides contextual information regarding course timing.
     */
    @Field(targetType = FieldType.OBJECT_ID)
    private String semester;

    /** 
     * The unique code that identifies the course.
     * It is important to ensure that this code adheres to specific format rules to avoid duplicates.
     */
    private String code;

    /** 
     * The name of the course.
     * This field should be descriptive enough to convey the subject matter of the course.
     */
    private String name;
}
