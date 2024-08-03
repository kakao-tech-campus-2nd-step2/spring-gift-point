package gift.web.exception.kakaoapi;

import org.springframework.http.HttpStatusCode;

public class KakaoApiException extends RuntimeException {
    private final HttpStatusCode statusCode;

    public KakaoApiException(HttpStatusCode statusCode, String message) {
        super(statusCode + message);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
