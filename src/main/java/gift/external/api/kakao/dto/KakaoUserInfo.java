package gift.external.api.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserInfo(
    Long id,
    KakaoAccount kakaoAccount
) {
    public record KakaoAccount(
        Profile profile,
        Boolean hasEmail,
        String email
    ) {}

    public record Profile(
        String nickname
    ) {}
}
