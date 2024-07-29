package gift.member.entity;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class KakaoTokenInfo {

    private String kakaoAccessToken;

    private LocalDateTime accessTokenExpiredAt;

    private String kakaoRefreshToken;

    private static final long EXPECTED_DELAY = 2L;

    protected KakaoTokenInfo() {
    }

    public KakaoTokenInfo(String kakaoAccessToken,
                          LocalDateTime accessTokenExpiredAt,
                          String kakaoRefreshToken) {
        this.kakaoAccessToken = kakaoAccessToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.kakaoRefreshToken = kakaoRefreshToken;
    }

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }

    public LocalDateTime getAccessTokenExpiredAt() {
        return accessTokenExpiredAt;
    }

    public String getKakaoRefreshToken() {
        return kakaoRefreshToken;
    }

    public boolean isExpired() {
        return accessTokenExpiredAt.isAfter(LocalDateTime.now()
                                                         .plusSeconds(EXPECTED_DELAY));
    }

    public void refresh(KakaoTokenInfo kakaoTokenInfo) {
        this.kakaoAccessToken = kakaoTokenInfo.getKakaoAccessToken();
        this.accessTokenExpiredAt = kakaoTokenInfo.getAccessTokenExpiredAt();
        this.kakaoRefreshToken = kakaoTokenInfo.getKakaoRefreshToken();
    }

}
