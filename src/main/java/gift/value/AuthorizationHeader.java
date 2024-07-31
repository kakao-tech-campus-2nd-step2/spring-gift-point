package gift.value;

public class AuthorizationHeader {
    private final String token;

    public AuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        this.token = authorizationHeader.substring(7); // "Bearer " 이후의 실제 토큰 값
    }

    public String getToken() {
        return token;
    }
}