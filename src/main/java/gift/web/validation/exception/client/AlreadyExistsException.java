package gift.web.validation.exception.client;

import static gift.web.validation.exception.code.ErrorStatus.ALREADY_EXISTS;

import gift.web.validation.exception.CustomException;
import gift.web.validation.exception.code.ErrorStatus;

public class AlreadyExistsException extends CustomException {

    private static final String ERROR_MESSAGE = "이미 존재하는 자원입니다. 중복 값: %s";

    public AlreadyExistsException(String resource) {
        super(ERROR_MESSAGE.formatted(resource));
    }

    public AlreadyExistsException(String resource, Throwable cause) {
        super(ERROR_MESSAGE.formatted(resource), cause);
    }

    public AlreadyExistsException(Throwable cause) {
        super(cause);
    }

    @Override
    public ErrorStatus getErrorStatus() {
        return ALREADY_EXISTS;
    }
}
