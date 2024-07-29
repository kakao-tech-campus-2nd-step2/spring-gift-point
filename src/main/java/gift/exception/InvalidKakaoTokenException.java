package gift.exception;

public class InvalidKakaoTokenException extends RuntimeException {
    public InvalidKakaoTokenException(String message) {
        super(message);
    }
}
