package gift.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorResult {
    private final String code;
    private final String message;

    private final Map<String, String> validation = new HashMap<>();

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getValidation() {
        return validation;
    }

    public ErrorResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
