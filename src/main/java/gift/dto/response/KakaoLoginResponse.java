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
}
