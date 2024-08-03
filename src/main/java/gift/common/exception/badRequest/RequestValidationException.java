package gift.common.exception.badRequest;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestValidationException extends CustomException {

    public RequestValidationException() {
        super(ErrorCode.REQUEST_VALIDATION, HttpStatus.BAD_REQUEST);
    }

    public RequestValidationException(String message) {
        super(ErrorCode.REQUEST_VALIDATION, HttpStatus.BAD_REQUEST);
        ErrorCode.REQUEST_VALIDATION.setCustomMessage(message);
    }

}
