package gift.web.validation.exception;

import gift.web.validation.exception.code.ErrorStatus;

/**
 * 커스텀 예외를 정의하고자 하는 경우 상속하여야 한다.
 */
public abstract class CustomException extends RuntimeException {

    protected CustomException() {
        super();
    }

    protected CustomException(String message) {
        super(message);
    }

    protected CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    protected CustomException(Throwable cause) {
        super(cause);
    }

    /**
     * 예외가 어떤 {@link ErrorStatus} 를 가지는지 반환한다.
     * @return {@link ErrorStatus}
     */
    public abstract ErrorStatus getErrorStatus();

}