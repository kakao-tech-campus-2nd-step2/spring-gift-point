package gift.controller;

import gift.config.properties.KakaoProperties;
import gift.controller.api.KakaoApi;
import gift.service.KakaoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/kakao")
public class KakaoController implements KakaoApi {

    private final KakaoService kakaoService;
    private final KakaoProperties kakaoProperties;

    public KakaoController(KakaoService kakaoService, KakaoProperties kakaoProperties) {
        this.kakaoService = kakaoService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/set-token")
    public ResponseEntity<Void> redirectSetToken(@RequestAttribute("memberId") Long memberId) {
        var headers = getRedirectHeader(kakaoProperties.tokenUri(), memberId);
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/token")
    public ResponseEntity<Void> setToken(@RequestParam String code, @RequestParam String state) {
        var memberId = Long.valueOf(state);
        kakaoService.saveKakaoToken(memberId, code);
        return ResponseEntity.noContent().build();
    }

    private HttpHeaders getRedirectHeader(String redirectUri, Long memberId) {
        var headers = new HttpHeaders();
        String redirectLocation = kakaoProperties.oauthBaseUri() + "&client_id=" + kakaoProperties.restApiKey() + "&redirect_uri=" + redirectUri + "&state=" + memberId;
        headers.setLocation(URI.create(redirectLocation));
        return headers;
    }
}
