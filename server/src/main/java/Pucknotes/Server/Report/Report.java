package Pucknotes.Server.Report;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

/**
 * Represents a report entity in the application.
 * This class is mapped to the "reports" collection in MongoDB.
 * It contains the necessary fields to create and manage reports,
 * including an identifier, owner, type, item, title, description,
 * and the date of creation.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reports")
public class Report {

  /**
   * The unique identifier for the report.
   * This field is automatically managed by MongoDB.
   */
  @Id
  private String id;

  /**
   * The identifier of the owner of the report.
   * This field cannot be null as it is marked with @NonNull.
   */
  @Setter
  @Field(targetType = FieldType.OBJECT_ID)
  private String owner;

  /**
   * The type of the report.
   * This field is optional and may contain different values based on reporting needs.
   */
  @Setter
  private String type;

  /**
   * The identifier of the associated item.
   * This field may be null, indicating that the report is not associated with any item.
   */
  @Setter
  @Field(targetType = FieldType.OBJECT_ID)
  private String item;

  @Setter
  private String title;

  /**
   * The description of the report.
   * This field cannot be null and must be set at creation.
   */
  @Setter
  private String description;

  /**
   * The date when the report was created.
   * This field is initialized with the current date and time when a new
   * report object is created.
   */
  private Date createdDate = new Date();
}
