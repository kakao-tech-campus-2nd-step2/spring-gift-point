package gift.dto;

public class KakaoUserInfoDTO {

    private long oauthId;

    private String email;

    private String kakaoAccessToken;

    public KakaoUserInfoDTO() {
    }

    public KakaoUserInfoDTO(long oauthId, String email, String kakaoAccessToken) {
        this.oauthId = oauthId;
        this.email = email;
        this.kakaoAccessToken = kakaoAccessToken;
    }

    public long getOauthId() {
        return oauthId;
    }

    public String getEmail() {
        return email;
    }

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }
}
