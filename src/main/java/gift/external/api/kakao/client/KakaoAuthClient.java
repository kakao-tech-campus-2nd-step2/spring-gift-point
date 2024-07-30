package gift.external.api.kakao.client;

import gift.external.api.kakao.dto.KakaoToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface KakaoAuthClient {

    @PostExchange(value = "/oauth/token")
    KakaoToken getAccessToken(@RequestBody MultiValueMap<String, String> body);
}
