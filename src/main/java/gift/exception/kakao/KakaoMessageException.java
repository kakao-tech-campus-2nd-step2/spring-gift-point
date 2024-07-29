package gift.exception.kakao;

public class KakaoMessageException extends RuntimeException {

    private static final String SEND_ERROR_MESSAGE = "카카오톡 메시지 전송에 실패하였습니다.";

    public KakaoMessageException() {
        super(SEND_ERROR_MESSAGE);
    }

    public KakaoMessageException(String message) {
        super(message);
    }

    public KakaoMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public KakaoMessageException(Throwable cause) {
        super(cause);
    }

    protected KakaoMessageException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
