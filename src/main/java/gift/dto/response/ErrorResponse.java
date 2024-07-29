package gift.dto.response;

public record ErrorResponse(int statusCode, String message, String path) {
}
