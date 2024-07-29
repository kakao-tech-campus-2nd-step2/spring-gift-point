package gift.exception.option;

public class FailedRetryException extends RuntimeException {

    private static final String FAILED_RETRY_MESSAGE = "요청에 실패하였습니다. 다시 시도해주세요.";

    public FailedRetryException() {
        super(FAILED_RETRY_MESSAGE);
    }

    public FailedRetryException(String message) {
        super(message);
    }

    public FailedRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedRetryException(Throwable cause) {
        super(cause);
    }

    protected FailedRetryException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
