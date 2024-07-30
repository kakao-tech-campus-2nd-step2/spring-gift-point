package gift.dto;

import gift.exception.ErrorCode;
import jakarta.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


public class ErrorResponse {

    private HttpStatus status;
    private String message;
    private List<FieldError> errors;

    private ErrorResponse(final ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.errors = new ArrayList<>();
    }

    private ErrorResponse(final ErrorCode errorCode, final List<FieldError> fieldErrors) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.errors = fieldErrors;
    }

    public static ErrorResponse of(final ErrorCode errorCode, final BindingResult bindingResult) {
        return new ErrorResponse(errorCode, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode errorCode,
        final Set<ConstraintViolation<?>> constraintViolations) {
        return new ErrorResponse(errorCode, FieldError.of(constraintViolations));
    }

    public static ErrorResponse of(final ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(final ErrorCode errorCode, final List<FieldError> errors) {
        return new ErrorResponse(errorCode, errors);
    }

    public static ErrorResponse of(final ErrorCode code, final String missingParameterName) {
        return new ErrorResponse(code, FieldError.of(missingParameterName, "", "값을 입력해야합니다"));
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public static class FieldError {

        private String field;
        private String value;
        private String reason;

        public FieldError() {
        }

        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value,
            final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        //bindingError
        public static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors
                = bindingResult.getFieldErrors();

            return fieldErrors.stream()
                .map(error -> new FieldError(
                    error.getField(),
                    error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                    error.getDefaultMessage()
                ))
                .collect(Collectors.toList());
        }

        public static List<FieldError> of(final Set<ConstraintViolation<?>> constraintViolations) {
            List<ConstraintViolation<?>> lists = new ArrayList<>(constraintViolations);
            return lists.stream()
                .map(error -> new FieldError(
                    error.getPropertyPath().toString(),
                    "",
                    error.getMessageTemplate()
                ))
                .collect(Collectors.toList());
        }

        public String getField() {
            return field;
        }

        public String getValue() {
            return value;
        }

        public String getReason() {
            return reason;
        }
    }
}


