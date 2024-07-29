package gift.error;

import java.util.List;
import java.util.Objects;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ErrorResponse {

    private List<ErrorField> errors;

    public ErrorResponse(List<ErrorField> errors) {
        this.errors = errors;
    }

    public List<ErrorField> getErrors(){
        return errors;
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(ErrorField.of(bindingResult));
    }

    public static class ErrorField {
        private String field;
        private String value;
        private String reason;

        public ErrorField(String field, String value, String defaultMessage) {
            this.field = field;
            this.value = value;
            this.reason = defaultMessage;
        }

        public String getField(){
            return field;
        }

        public String getValue(){
            return value;
        }

        public String getReason(){
            return reason;
        }

        public static List<ErrorField> of(BindingResult bindingResult) {
            return bindingResult.getAllErrors().stream().map(error ->
                new ErrorField(((FieldError) error).getField(), Objects.requireNonNull(
                    ((FieldError) error).getRejectedValue()).toString(),
                    error.getDefaultMessage())).toList();
        }
    }
}
