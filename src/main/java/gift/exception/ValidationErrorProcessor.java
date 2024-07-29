package gift.exception;

import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationErrorProcessor {

    public static String processFieldErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(ValidationErrorProcessor::mapFieldErrorToMessage)
                .collect(Collectors.joining("\n"));
    }

    private static String mapFieldErrorToMessage(FieldError error) {
        if ("Size".equals(error.getCode())) {
            return ErrorCode.INVALID_NAME_SIZE.getMessage();
        } else if ("Pattern".equals(error.getCode())) {
            return ErrorCode.INVALID_NAME_PATTERN.getMessage();
        }
        return error.getDefaultMessage();
    }
}
