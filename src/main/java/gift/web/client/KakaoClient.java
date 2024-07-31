package gift.web.client;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import gift.authentication.token.KakaoToken;
import gift.web.client.dto.KakaoInfo;
import gift.web.client.dto.KakaoMessageResult;
import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoClient")
public interface KakaoClient {

    @PostMapping
    KakaoInfo getKakaoInfo(
        URI uri,
        @RequestHeader(AUTHORIZATION) String accessToken);

    @PostMapping
    KakaoToken getToken(
        URI uri,
        @RequestParam("code") String code,
        @RequestParam("client_id") String clientId,
        @RequestParam("redirect_uri") String redirectUrl,
        @RequestParam("grant_type") String grantType);

    /**
     * 카카오톡 메시지 - 나에게 보내기
     * @param uri {@link https://kapi.kakao.com/v2/api/talk/memo/default/send} 으로 고정
     * @param accessToken Bearer Token
     * @param templateObject 메시지 구성 요소를 담은 객체(Object) 피드, 리스트, 위치, 커머스, 텍스트, 캘린더 중 하나
     */
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoMessageResult sendMessage(
        URI uri,
        @RequestHeader(AUTHORIZATION) String accessToken,
        @RequestBody String templateObject
    );
}
