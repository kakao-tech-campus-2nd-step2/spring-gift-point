package gift.auth.token;

public record AuthToken(
        String accessToken,
        String refreshToken,
        String grantType,
        Long expiresIn
) {
}
