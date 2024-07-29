package gift.user;

public class KakaoUserDTO {
    Long id;
    String accessToken;
    String refreshToken;

    public KakaoUserDTO() {
    }

    public KakaoUserDTO(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
