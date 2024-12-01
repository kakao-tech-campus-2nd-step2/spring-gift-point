package gift.component.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record KakaoProperties(

    @Value("${kakao.clientId}")  String kakaoClientId,

    @Value("${kakao.redirectUrl}")  String kakaoRedirectUrl
) {
}
