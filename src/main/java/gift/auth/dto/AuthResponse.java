package gift.auth.dto;

public record AuthResponse(
        String token
) {
    public static AuthResponse of(String token) {
        return new AuthResponse(token);
    }

}