package gift.web.validation.exception.client;

import static gift.web.validation.exception.code.ErrorStatus.UNAUTHORIZED_INVALID_CREDENTIALS;

import gift.web.validation.exception.CustomException;
import gift.web.validation.exception.code.ErrorStatus;

public class InvalidCredentialsException extends CustomException {

    private static final String ERROR_MESSAGE = "유효하지 않은 신원 정보입니다.";

    public InvalidCredentialsException() {
        super(ERROR_MESSAGE);
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }

    @Override
    public ErrorStatus getErrorStatus() {
        return UNAUTHORIZED_INVALID_CREDENTIALS;
    }

}
