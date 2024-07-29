package gift.exception;
import org.springframework.http.HttpStatus;

public class ErrorCode {

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    public ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
