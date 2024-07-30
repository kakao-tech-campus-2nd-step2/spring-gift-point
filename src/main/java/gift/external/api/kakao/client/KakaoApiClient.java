package gift.external.api.kakao.client;

import com.fasterxml.jackson.databind.JsonNode;
import gift.external.api.kakao.dto.KakaoUserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface KakaoApiClient {

    @GetExchange(value = "/v1/user/access_token_info")
    JsonNode getAccessTokenInfo(@RequestHeader("Authorization") HttpHeaders headers);

    @GetExchange(value = "/v2/user/me")
    KakaoUserInfo getUserInfo(@RequestHeader("Authorization") HttpHeaders headers);

    @PostExchange(value = "/v2/api/talk/memo/default/send")
    JsonNode sendMessageToMe(@RequestHeader("Authorization") HttpHeaders headers, @RequestBody MultiValueMap<String, String> body);
}
