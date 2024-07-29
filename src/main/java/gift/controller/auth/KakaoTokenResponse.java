package gift.controller.auth;

public class KakaoTokenResponse {

    String accessToken;

    public KakaoTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
