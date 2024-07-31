package gift.global.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.exception.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 * 실패 HTTP 응답 생성을 보조하는 유틸리티 클래스
 */
public class ErrorApiResponse extends BasicApiResponse {

    private final String message;
    private final String errorCode;

    public ErrorApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "message", required = true) String message,
        @JsonProperty(value = "error-code", required = true) String errorCode
    ) {
        super(statusCode, false);
        this.message = message;
        this.errorCode = errorCode;
    }
    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    // status 코드에 대한 기본적인 RESTful 응답을 생성
    public static ResponseEntity<ErrorApiResponse> of(String message, String errorCode, HttpStatusCode statusCode) {
        return ResponseEntity
            .status(statusCode)
            .body(new ErrorApiResponse(statusCode, message, errorCode));
    }

    public static ResponseEntity<ErrorApiResponse> of(ServerException cause, HttpStatusCode statusCode) {
        return ErrorApiResponse.of(cause.getMessage(), cause.getErrorCode().getCode(), statusCode);
    }

    // HTTP code 401에 대한 응답 생성
    public static ResponseEntity<ErrorApiResponse> unauthorized(ServerException cause) {
        return ErrorApiResponse.of(cause, HttpStatus.UNAUTHORIZED);
    }

    // HTTP code 403에 대한 응답 생성
    public static ResponseEntity<ErrorApiResponse> forbidden(ServerException cause) {
        return ErrorApiResponse.of(cause, HttpStatus.FORBIDDEN);
    }

    // HTTP code 404에 대한 응답 생성
    public static ResponseEntity<ErrorApiResponse> notFound(ServerException cause) {
        return ErrorApiResponse.of(cause, HttpStatus.NOT_FOUND);
    }

    // HTTP code 409에 대한 응답 생성
    public static ResponseEntity<ErrorApiResponse> conflict(ServerException cause) {
        return ErrorApiResponse.of(cause, HttpStatus.CONFLICT);
    }
}
