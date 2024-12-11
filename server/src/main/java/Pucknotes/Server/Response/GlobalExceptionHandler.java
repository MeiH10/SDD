package Pucknotes.Server.Response;

import Pucknotes.Server.Response.Types.ResourceConflictException;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Response.Types.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GlobalExceptionHandler is a centralized error handling component that intercepts exceptions thrown by
 * the application's controllers. It provides appropriate HTTP responses based on the type of exception
 * encountered, ensuring that clients receive meaningful error messages and status codes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles all exceptions that are not specifically handled by other methods in this class.
   * It returns a response with a generic error message and an HTTP 500 status to indicate an internal server error.
   *
   * @param ex the exception thrown
   * @return a ResponseEntity containing an APIResponse with the error message and HTTP status
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<APIResponse<Object>> handleException(Exception ex) {
    // Ensure that if the error message is null, a default message is provided.
    String errorMessage = ex.getMessage() != null
      ? ex.getMessage()
      : "An unexpected error occurred.";
    APIResponse<Object> response = APIResponse.bad(errorMessage); // Creating a bad response from APIResponse.
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // Return with an HTTP 500 status.
  }

  /**
   * Handles ResourceNotFoundExceptions, which should be thrown when a requested resource cannot be found.
   * It returns an HTTP 404 status along with the error message from the exception.
   *
   * @param ex the ResourceNotFoundException thrown
   * @return a ResponseEntity containing an APIResponse with the error message and HTTP status
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<APIResponse<Object>> handleResourceNotFoundException(
    ResourceNotFoundException ex
  ) {
    APIResponse<Object> response = APIResponse.bad(ex.getMessage()); // Creating a bad response with the error message.
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // Return with an HTTP 404 status.
  }

  /**
   * Handles IllegalArgumentExceptions, which indicate that a provided argument is not acceptable.
   * It returns an HTTP 400 status along with the error message from the exception.
   *
   * @param ex the IllegalArgumentException thrown
   * @return a ResponseEntity containing an APIResponse with the error message and HTTP status
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<APIResponse<Object>> handleBadRequestException(
    IllegalArgumentException ex
  ) {
    APIResponse<Object> response = APIResponse.bad(ex.getMessage()); // Creating a bad response from the error message.
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // Return with an HTTP 400 status.
  }

  /**
   * Handles ResourceConflictExceptions, which indicate an attempt to create a resource that
   * conflicts with an existing resource. It returns an HTTP 409 status along with the error message.
   *
   * @param ex the ResourceConflictException thrown
   * @return a ResponseEntity containing an APIResponse with the error message and HTTP status
   */
  @ExceptionHandler(ResourceConflictException.class)
  public ResponseEntity<APIResponse<Object>> handleResourceConflictException(
    ResourceConflictException ex
  ) {
    APIResponse<Object> response = APIResponse.bad(ex.getMessage()); // Creating a bad response from the error message.
    return new ResponseEntity<>(response, HttpStatus.CONFLICT); // Return with an HTTP 409 status.
  }

  /**
   * Handles UnauthorizedExceptions, indicating that the request requires user authentication.
   * It returns an HTTP 403 status along with the error message.
   *
   * @param ex the UnauthorizedException thrown
   * @return a ResponseEntity containing an APIResponse with the error message and HTTP status
   */
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<APIResponse<Object>> handleUnauthorizedException(
    UnauthorizedException ex
  ) {
    APIResponse<Object> response = APIResponse.bad(ex.getMessage()); // Creating a bad response from the error message.
    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // Return with an HTTP 403 status.
  }
}
