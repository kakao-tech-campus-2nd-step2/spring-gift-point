package gift.controller;

import gift.config.properties.KakaoProperties;
import gift.service.KakaoService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "KAKAO")
public class KakaoController {

    private final KakaoService kakaoService;
    private final KakaoProperties kakaoProperties;
    private static final String OAUTH_BASE_URL = "https://kauth.kakao.com/oauth/authorize?response_type=code&scope=account_email,talk_message";

    public KakaoController(KakaoService kakaoService, KakaoProperties kakaoProperties) {
        this.kakaoService = kakaoService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/set-token")
    public ResponseEntity<Void> redirectSetToken(@RequestAttribute("memberId") Long memberId) {
        var headers = getRedirectHeader(kakaoProperties.tokenUri(), memberId);
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Hidden
    @GetMapping("/token")
    public ResponseEntity<Void> setToken(@RequestParam String code, @RequestParam String state) {
        var memberId = Long.valueOf(state);
        kakaoService.saveKakaoToken(memberId, code);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-oauth")
    public ResponseEntity<Void> redirectOAuth() {
        var headers = getRedirectHeader(kakaoProperties.redirectUri());
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    private HttpHeaders getRedirectHeader(String redirectUri) {
        var headers = new HttpHeaders();
        String redirectLocation = OAUTH_BASE_URL + "&client_id=" + kakaoProperties.restApiKey() + "&redirect_uri=" + redirectUri;
        headers.setLocation(URI.create(redirectLocation));
        return headers;
    }

    private HttpHeaders getRedirectHeader(String redirectUri, Long memberId) {
        var headers = new HttpHeaders();
        String redirectLocation = OAUTH_BASE_URL + "&client_id=" + kakaoProperties.restApiKey() + "&redirect_uri=" + redirectUri + "&state=" + memberId;
        headers.setLocation(URI.create(redirectLocation));
        return headers;
    }
}
