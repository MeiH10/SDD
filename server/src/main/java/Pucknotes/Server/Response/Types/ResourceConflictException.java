package Pucknotes.Server.Response.Types;

/**
 * This class represents an exception that is thrown when there is a resource conflict 
 * in the server operations, such as trying to create a resource that already exists 
 * or when there is a versioning conflict during an update operation. 
 * It extends the RuntimeException class, making it an unchecked exception, 
 * which means it does not need to be declared in the method signatures.
 */
public class ResourceConflictException extends RuntimeException {

    /**
     * Constructs a new ResourceConflictException with the specified detail message.
     *
     * @param message The detail message that is saved for later retrieval by the 
     *                {@link Throwable#getMessage()} method. This message usually 
     *                describes the conflict that has occurred and why the exception 
     *                was thrown.
     */
    public ResourceConflictException(String message) {
        super(message); // Call the superclass constructor with the error message
    }
}
