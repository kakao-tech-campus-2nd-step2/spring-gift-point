package gift.exception.option;

public class DeleteOptionsException extends RuntimeException {

    private static final String DELETE_OPTIONS_MESSAGE = "남은 상품 옵션이 없으므로 해당 옵션을 삭제할 수 없습니다.";

    public DeleteOptionsException() {
        super(DELETE_OPTIONS_MESSAGE);
    }

    public DeleteOptionsException(String message) {
        super(message);
    }

    public DeleteOptionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteOptionsException(Throwable cause) {
        super(cause);
    }

    protected DeleteOptionsException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
