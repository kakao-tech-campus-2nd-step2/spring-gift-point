package gift.domain.Auth;

public record LoginRequest(
        String email,
        String password
) {
}
