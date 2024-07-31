package gift.web.validation.exception.server;

import static gift.web.validation.exception.code.ErrorStatus.INTERNAL_SERVER_ERROR;

import gift.web.validation.exception.CustomException;
import gift.web.validation.exception.code.ErrorStatus;

public class InternalServerException extends CustomException {

    private static final String ERROR_MESSAGE = "서버에서 오류가 발생했습니다.";

    public InternalServerException() {
        super(ERROR_MESSAGE);
    }

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerException(Throwable cause) {
        super(cause);
    }

    @Override
    public ErrorStatus getErrorStatus() {
        return INTERNAL_SERVER_ERROR;
    }

}
