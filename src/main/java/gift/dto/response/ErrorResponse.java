package gift.dto.response;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {

    private final int code;
    private final String message;
    private final Map<String, String> validation;

    public ErrorResponse(int code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getValidation() {
        return validation;
    }

    public static class ErrorResponseBuilder {
        private int code;
        private String message;
        private Map<String, String> validation;

        public ErrorResponseBuilder code(int code) {
            this.code = code;
            return this;
        }

        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder validation(Map<String, String> validation) {
            this.validation = validation;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(code, message, validation);
        }
    }

}
