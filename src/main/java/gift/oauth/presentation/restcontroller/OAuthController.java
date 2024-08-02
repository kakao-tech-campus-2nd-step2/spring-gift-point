package gift.oauth.presentation.restcontroller;

import gift.docs.oauth.OAuthApiDocs;
import gift.global.authentication.dto.AuthResponse;
import gift.oauth.business.dto.KakaoParam;
import gift.oauth.business.dto.OAuthParam;
import gift.oauth.business.service.OAuthService;
import gift.oauth.presentation.config.KakaoConfig;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class OAuthController implements OAuthApiDocs{

    private final OAuthService oauthService;
    private final KakaoConfig kakaoConfig;

    public OAuthController(OAuthService oauthService, KakaoConfig kakaoConfig) {
        this.oauthService = oauthService;
        this.kakaoConfig = kakaoConfig;
    }

    @GetMapping
    public ResponseEntity<AuthResponse> getKakaoCode(@RequestParam String code) {
        OAuthParam param = new KakaoParam(kakaoConfig, code);
        var authOutInit = oauthService.loginOrRegister(param);
        var authResponse = new AuthResponse(authOutInit.email(), authOutInit.accessToken());
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/api/oauth/kakao")
    public ResponseEntity<Void> singInKaKao() {
        var redirectUrl = UriComponentsBuilder.fromUriString(kakaoConfig.codeUrl())
            .queryParam("scope", "talk_message")
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", kakaoConfig.redirectUri())
            .queryParam("client_id", kakaoConfig.clientId())
            .toUriString();

        var headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
