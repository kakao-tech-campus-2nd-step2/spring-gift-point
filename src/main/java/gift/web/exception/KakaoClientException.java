package gift.web.exception;

import org.springframework.http.HttpStatusCode;

public class KakaoClientException extends KakaoApiException {
    public KakaoClientException(HttpStatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
