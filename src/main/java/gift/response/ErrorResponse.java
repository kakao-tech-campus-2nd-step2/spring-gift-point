package gift.response;

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

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getValidation() {
        return validation;
    }
}