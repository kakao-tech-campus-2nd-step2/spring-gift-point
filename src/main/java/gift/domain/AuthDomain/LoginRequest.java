package gift.domain.AuthDomain;

public record LoginRequest(
        String email,
        String password
) {
}
