package gift.domain.Auth;

public record TokenResponse(
        String email,
        String token
) {
}
