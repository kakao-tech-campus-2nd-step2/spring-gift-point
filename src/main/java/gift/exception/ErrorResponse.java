package gift.exception;

import org.springframework.http.HttpStatusCode;

public class ErrorResponse {
    private final HttpStatusCode httpStatusCode;
    private final String message;

    public ErrorResponse(HttpStatusCode httpStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getMessage() {
        return message;
    }
}
