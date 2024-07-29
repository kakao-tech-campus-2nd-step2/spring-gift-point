package gift.exception;

public class KakaoAuthException extends RuntimeException {

    public KakaoAuthException(String message) {
        super(message);
    }

    public KakaoAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}