package gift.exception;

public class KakaoMessageException extends ConflictException{

    public KakaoMessageException() {
        super("Kakao Message Exception");
    }
    public KakaoMessageException(String message) {
        super(message);
    }
}
