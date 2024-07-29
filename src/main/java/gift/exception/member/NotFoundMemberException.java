package gift.exception.member;

public class NotFoundMemberException extends RuntimeException {

    private static final String NOT_FOUND_MEMBER_MESSAGE = "아이디 또는 비밀번호가 일치하지 않습니다.";

    public NotFoundMemberException() {
        super(NOT_FOUND_MEMBER_MESSAGE);
    }

    public NotFoundMemberException(String message) {
        super(message);
    }

    public NotFoundMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundMemberException(Throwable cause) {
        super(cause);
    }

    protected NotFoundMemberException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
