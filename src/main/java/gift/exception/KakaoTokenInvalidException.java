package gift.exception;

public class KakaoTokenInvalidException extends ConflictException {

    public KakaoTokenInvalidException() {
        super("Kakao token is invalid");
    }

    public KakaoTokenInvalidException(String message) {
        super(message);
    }
}
