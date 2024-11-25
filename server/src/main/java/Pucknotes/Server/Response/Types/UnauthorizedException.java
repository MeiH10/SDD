package Pucknotes.Server.Response.Types;

/**
 * The UnauthorizedException class extends the RuntimeException class
 * to represent an exception that is thrown when a user attempts to
 * access a resource without proper authorization. This exception is 
 * used to signal that the current user is not permitted to perform 
 * the requested action, typically due to missing or invalid credentials.
 */
public class UnauthorizedException extends RuntimeException {
    
    /**
     * Constructs a new UnauthorizedException with the specified detail message.
     *
     * @param message The detail message which describes the reason for the exception.
     *                This message will be included in the exception's output to help 
     *                diagnose the issue.
     */
    public UnauthorizedException(String message) {
        // Call the constructor of the superclass (RuntimeException) with the provided message.
        super(message); // It is crucial to provide a meaningful message for clarity.
    }
}
