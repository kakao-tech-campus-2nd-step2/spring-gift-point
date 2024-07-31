package gift.config;

public class AuthorizationHeader {
    private final String authHeader;

    public AuthorizationHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("Invalid authorization header");
        }
        this.authHeader = authHeader;
    }

    public String getToken() {
        return authHeader.substring(7);
    }

    @Override
    public String toString() {
        return authHeader;
    }
}
