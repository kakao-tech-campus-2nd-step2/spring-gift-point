package gift.permission.kakao.dto;

import gift.global.component.KakaoProperties;
import gift.global.model.MultiValueMapConvertibleDto;

// kakao auth code를 요청할 때 사용할 dto
public record KakaoAuthRequestDto(
    String clientId,
    String redirectUri,
    String responseType
) implements MultiValueMapConvertibleDto {

    public static KakaoAuthRequestDto of(KakaoProperties kakaoProperties) {
        return new KakaoAuthRequestDto(kakaoProperties.clientId(), kakaoProperties.redirectUri(),
            kakaoProperties.responseType());
    }
}
