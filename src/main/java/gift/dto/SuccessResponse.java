package gift.dto;

import org.springframework.http.HttpStatus;

public record SuccessResponse(HttpStatus httpStatus, String message) {
}
