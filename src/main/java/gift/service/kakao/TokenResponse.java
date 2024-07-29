package gift.service.kakao;

public class TokenResponse {

    private String accessToken;
    private String jwt;

    public TokenResponse(String accessToken, String jwt) {
        this.accessToken = accessToken;
        this.jwt = jwt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getJwt() {
        return jwt;
    }

}
