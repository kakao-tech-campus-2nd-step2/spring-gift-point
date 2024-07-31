package gift.product.exception.kakao;

import org.springframework.http.HttpStatus;

public class KakaoApiException extends RuntimeException {
    private final HttpStatus status;

    public KakaoApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
