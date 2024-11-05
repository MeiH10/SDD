package Pucknotes.Server.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {
    @Getter private boolean good;
    @Getter private T data;
    @Getter private String error;

    public static <T> APIResponse<T> good(T data) {
        return new APIResponse<>(true, data, null);
    }

    public static <T> APIResponse<T> bad(String error) {
        return new APIResponse<>(false, null, error);
    }
}

