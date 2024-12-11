package Pucknotes.Server.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * APIResponse is a generic class that represents the structure of a response
 * sent by the API. It can encapsulate whether the response is successful,
 * the related data of a successful response, or an error message if the
 * response is unsuccessful.
 *
 * @param <T> The type of data contained in the response.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {

  // Indicates whether the API response is successful.
  @Getter
  private boolean good;

  // The data returned in the API response. This will be null if the response is unsuccessful.
  @Getter
  private T data;

  // An error message returned in the API response. This will be null if the response is successful.
  @Getter
  private String error;

  /**
   * Creates a new successful APIResponse object containing the provided data.
   *
   * @param data The data to be included in the successful response.
   * @param <T> The type of the data that is being returned.
   * @return A new instance of APIResponse with true indicating success and the provided data.
   */
  public static <T> APIResponse<T> good(T data) {
    // Create a successful response with the provided data.
    // The error message is set to null as this is a good response.
    return new APIResponse<>(true, data, null);
  }

  /**
   * Creates a new unsuccessful APIResponse object containing the provided error message.
   *
   * @param error The error message to be included in the unsuccessful response.
   * @param <T> The type of data that could have been returned, which is irrelevant in this case.
   * @return A new instance of APIResponse with false indicating failure and the provided error message.
   */
  public static <T> APIResponse<T> bad(String error) {
    // Create an unsuccessful response with the provided error message.
    // The data is set to null since there is no data to return for an error response.
    return new APIResponse<>(false, null, error);
  }
}
