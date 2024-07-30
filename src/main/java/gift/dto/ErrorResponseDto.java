package gift.dto;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class ErrorResponseDto {

    private String message;
    private HttpStatus status;
    private Map<String, String> validation;

    public ErrorResponseDto(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getValidation() {
        return validation;
    }
}