package gift.dto.kakao;

import gift.domain.KakaoToken;

public record KakaoTokenDto(
    String access_token,
    String refresh_token
) {

    public KakaoToken toEntity(String email) {
        return new KakaoToken(email, access_token, refresh_token);
    }
}
