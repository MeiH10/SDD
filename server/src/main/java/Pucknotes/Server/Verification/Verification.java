package Pucknotes.Server.Verification;

import Pucknotes.Server.Account.Account;
import com.mongodb.lang.NonNull;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The Verification class represents a verification object used in the application.
 * It is stored in a MongoDB collection named "verifications".
 * This class holds information about a verification, including a unique identifier,
 * details about the account being verified, and a token used for the verification process.
 *
 * The class uses Lombok annotations to reduce boilerplate code for getters, setters,
 * and constructors.
 */
@Getter
@NoArgsConstructor // Generates a no-argument constructor
@RequiredArgsConstructor // Generates a constructor with required arguments
@Document(collection = "verifications") // Specifies the MongoDB collection to store this document
public class Verification {

  @Id // Indicates that this field is the unique identifier for the document
  private String id;

  @Setter // Allows setting the details field from outside the class
  @NonNull // Indicates that this field must be provided when creating an instance of this class
  private Account details;

  @Setter // Allows setting the token field from outside the class
  private String token = UUID.randomUUID().toString(); // Automatically generates a new UUID token upon creation
}
