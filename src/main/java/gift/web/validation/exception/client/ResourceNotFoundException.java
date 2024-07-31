package gift.web.validation.exception.client;

import static gift.web.validation.exception.code.ErrorStatus.NOT_FOUND;

import gift.web.validation.exception.CustomException;
import gift.web.validation.exception.code.ErrorStatus;

public class ResourceNotFoundException extends CustomException {

    private static final String ERROR_MESSAGE = "해당 리소스를 찾을 수 없습니다. %s: %s";

    public ResourceNotFoundException(String type, String identifier) {
        super(ERROR_MESSAGE.formatted(type, identifier));
    }

    protected ResourceNotFoundException(String type, String identifier, Throwable cause) {
        super(ERROR_MESSAGE.formatted(type, identifier), cause);
    }

    protected ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public ErrorStatus getErrorStatus() {
        return NOT_FOUND;
    }
}
