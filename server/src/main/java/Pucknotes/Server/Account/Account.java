package Pucknotes.Server.Account;

import com.mongodb.lang.NonNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class represents an account in the Pucknotes application.
 * It is mapped to the "accounts" collection in a MongoDB database.
 */
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "accounts")
public class Account {

  /**
   * The unique identifier for the account, automatically generated by MongoDB.
   */
  @Id
  @Setter
  private String id;

  /**
   * The email address associated with the account.
   * This field is required and cannot be null.
   */
  @Setter
  @NonNull
  private String email;

  /**
   * The username associated with the account.
   * This field is required and cannot be null.
   */
  @Setter
  @NonNull
  private String username;

  /**
   * The password associated with the account.
   * This field is required and cannot be null.
   */
  @Setter
  @NonNull
  private String password;

  @Setter
  @NonNull
  private int role = 2;
}
