package gift.exception;

public class KakaoServiceException extends RuntimeException {

    public KakaoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}