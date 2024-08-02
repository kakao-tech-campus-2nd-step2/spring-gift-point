package gift.global.authentication.dto;

public record AuthResponse(
    String email,
    String accessToken
) {

}
