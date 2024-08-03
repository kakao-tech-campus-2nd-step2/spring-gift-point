package gift.permission.kakao.dto;

import gift.global.component.KakaoProperties;
import gift.global.model.MultiValueMapConvertibleDto;

public record KaKaoTokenRequestBodyDto(
    String grantType,
    String clientId,
    String redirectUri,
    String code
) implements MultiValueMapConvertibleDto {

    public static KaKaoTokenRequestBodyDto of(KakaoProperties kakaoProperties, String code) {
        return new KaKaoTokenRequestBodyDto(kakaoProperties.grantType(), kakaoProperties.clientId(),
            kakaoProperties.redirectUri(), code);
    }
}
