package gift.dto.common.apiResponse;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseGenerator {
    public static ResponseEntity<ApiResponseBody.SuccessBody<Void>> success(HttpStatus status,
        String message) {
        return new ResponseEntity<>(
            new ApiResponseBody.SuccessBody<>(String.valueOf(status.value()), message, null),
            status);
    }

    public static <D> ResponseEntity<ApiResponseBody.SuccessBody<D>> success(HttpStatus status,
        String message, D data) {
        return new ResponseEntity<>(
            new ApiResponseBody.SuccessBody<>(String.valueOf(status.value()), message, data),
            status);
    }

    public static ResponseEntity<ApiResponseBody.FailureBody> fail(HttpStatus status,
        String message) {
        return new ResponseEntity<>(
            new ApiResponseBody.FailureBody(String.valueOf(status), message, null), status);
    }

    //Todo: 에러 코드 만들기
    public static ResponseEntity<ApiResponseBody.FailureBody> fail(HttpStatus status,
        String message, String code) {
        return new ResponseEntity<>(
            new ApiResponseBody.FailureBody(String.valueOf(status), message, code), status);
    }
}
