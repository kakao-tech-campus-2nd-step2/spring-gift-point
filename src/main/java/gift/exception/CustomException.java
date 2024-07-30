package gift.exception;

import gift.dto.ErrorResponse;
import gift.dto.ErrorResponse.FieldError;
import java.util.ArrayList;
import java.util.List;

public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private List<FieldError> errors = new ArrayList<>();

    public CustomException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errors = errors;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
