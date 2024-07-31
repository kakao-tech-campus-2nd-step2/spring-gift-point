package gift.common.util;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        int status,
        T data
) {
    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return new ApiResponse<>(status.value(), data);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, T errorMessage) {
        return new ApiResponse<>(status.value(), errorMessage);
    }
}
