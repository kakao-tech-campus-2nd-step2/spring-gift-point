package gift;

public class AuthorizationToken {

    private final String token;

    public AuthorizationToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token format");
        }
        this.token = token.replace("Bearer ", "");
    }

    public String getToken() {
        return token;
    }
}