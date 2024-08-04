package gift.dto.response;

public class KakaoLoginResponse {
    private String authorizationCode;
    private String accessToken;
    private String email;

    public KakaoLoginResponse(String authorizationCode, String accessToken, String email) {
        this.authorizationCode = authorizationCode;
        this.accessToken = accessToken;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

}
