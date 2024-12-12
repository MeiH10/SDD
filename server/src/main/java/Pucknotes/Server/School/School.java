package Pucknotes.Server.School;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

/**
 * The School class represents a school entity in the MongoDB database.
 * It serves as a data model for school-related data, containing essential
 * information such as the school's unique identifier, name, and associated semester.
 *
 * The class uses Lombok annotations to automatically generate boilerplate code
 * such as constructors and getters. The school data is mapped to the "schools"
 * collection in MongoDB.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "schools")
public class School {

  /**
   * The unique identifier for the school.
   * This field is annotated with @Id to signify that it is the primary key
   * of the school entity.
   */
  @Id
  @NonNull
  private String id;

  /**
   * The name of the school.
   * This field represents the official name of the school.
   */
  @NonNull
  private String name;

  /**
   * The semester associated with the school.
   * This field is mapped to an ObjectId in MongoDB, indicating a reference
   * to a specific semester entity. It signifies that multiple semesters
   * may be associated with a school.
   */
  @Field(targetType = FieldType.OBJECT_ID)
  private String semester;
}
