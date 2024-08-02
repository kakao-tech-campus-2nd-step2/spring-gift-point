package gift.domain.AuthDomain;

public record TokenResponse(
        String email,
        String token
) {
}
