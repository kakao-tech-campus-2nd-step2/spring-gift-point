package gift.web.exception;

import org.springframework.http.HttpStatusCode;

public class KakaoServerException extends KakaoApiException {
    public KakaoServerException(HttpStatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
