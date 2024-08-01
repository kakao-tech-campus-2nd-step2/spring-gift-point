package gift.dto;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseDto {

    private final int code;
    private final String message;
    private final Map<String, String> validation;

    public ErrorResponseDto(String message, int code, Map<String, String> validation) {
        this.message = message;
        this.code = code;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Map<String, String> getValidation() {
        return validation;
    }
}