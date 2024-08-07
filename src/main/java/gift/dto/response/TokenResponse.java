package gift.dto.response;

public class TokenResponse {
    private String email;
    private String accessToken;

    public TokenResponse(String email, String accessToken) {
        this.email = email;
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
