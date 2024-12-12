package Pucknotes.Server.Response.Types;

/**
 * This class represents a custom exception that is thrown when a requested resource
 * is not found within the application. It extends the RuntimeException class, which means
 * that it is an unchecked exception and does not need to be declared in a method's
 * throws clause.
 */
public class ResourceNotFoundException extends RuntimeException {

  /**
   * Constructs a new ResourceNotFoundException with the specified detail message.
   *
   * @param message the detail message that provides more information about the exception.
   *                 This message can be used to notify the user of the specific resource
   *                 that could not be found.
   */
  public ResourceNotFoundException(String message) {
    super(message); // Call the constructor of the superclass (RuntimeException)
  }
}
