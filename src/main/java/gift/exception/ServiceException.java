package gift.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Map<String, String> validationErrors;

    public ServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.validationErrors = null;
    }

    public ServiceException(String message, HttpStatus httpStatus,
        Map<String, String> validationErrors) {
        super(message);
        this.httpStatus = httpStatus;
        this.validationErrors = validationErrors;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors != null ? validationErrors : Map.of(); // null인 경우 빈 맵 반환
    }
}