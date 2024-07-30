package gift.exception;

public class InvalidAccessTokenException extends RuntimeException {

    public InvalidAccessTokenException() {
        super("토큰 검증에 실패하였습니다.");
    }
}
