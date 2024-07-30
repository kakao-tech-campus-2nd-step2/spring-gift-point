package gift.dto.response;

import java.util.Map;

public class ErrorResponse {
    private String message;
    private final Integer status;
    private Map<String, String> errors;

    public ErrorResponse(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public ErrorResponse(Map<String, String> errors, Integer status) {
        this.errors = errors;
        this.status = status;
    }
}
