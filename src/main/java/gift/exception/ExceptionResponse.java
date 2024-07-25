package gift.exception;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {
    private int status;
    private HttpStatus httpStatus;
    private String message;

    public ExceptionResponse(ErrorCode error) {
        this.status = error.getStatus();
        this.message = error.getMessage();
        this.httpStatus = error.getHttpStatus();
    }

    public ExceptionResponse(Exception exception) {
        this.status = 400;
        this.message = exception.getMessage();
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public int getStatus() {
        return status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
