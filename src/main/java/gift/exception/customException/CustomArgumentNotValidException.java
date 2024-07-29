package gift.exception.customException;

import gift.exception.ErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class CustomArgumentNotValidException extends MethodArgumentNotValidException {

    private final ErrorCode errorCode;

    public CustomArgumentNotValidException(MethodParameter parameter, BindingResult bindingResult,
        ErrorCode errorCode) {
        super(parameter, bindingResult);
        this.errorCode = errorCode;
    }

    public CustomArgumentNotValidException(BindingResult bindingResult, ErrorCode errorCode) {
        super(null, bindingResult);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
