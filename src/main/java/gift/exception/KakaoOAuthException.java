package gift.exception;

import org.springframework.http.HttpStatusCode;

public class KakaoOAuthException extends RuntimeException {
    private final String errorCode;
    private final String statusCode;

    public KakaoOAuthException(String errorCode, HttpStatusCode statusCode) {
        super("Error code: " + errorCode + ", Description: " + statusCode);
        this.errorCode = errorCode;
        this.statusCode = statusCode.toString();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
