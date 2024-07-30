package gift.dto;

import gift.domain.KakaoToken;

public record KakaoTokenDTO(
    String access_token,
    String refresh_token
) {

    public KakaoToken toEntity(String email) {
        return new KakaoToken(email, access_token, refresh_token);
    }
}
