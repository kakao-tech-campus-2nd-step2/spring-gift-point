package gift.controller.oauth;

import gift.config.KakaoProperties;
import gift.dto.member.MemberResponse;
import gift.dto.oauth.KakaoScopeResponse;
import gift.dto.oauth.KakaoTokenResponse;
import gift.dto.oauth.KakaoUnlinkResponse;
import gift.dto.oauth.KakaoUserResponse;
import gift.service.oauth.KakaoOAuthService;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoOAuthController {

    private final KakaoProperties kakaoProperties;
    private final KakaoOAuthService kakaoOAuthService;

    public KakaoOAuthController(KakaoProperties kakaoProperties, KakaoOAuthService kakaoOAuthService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoOAuthService = kakaoOAuthService;
    }

    @GetMapping
    public ResponseEntity<Void> kakaoLogin() {
        String kakaoAuthUrl =
            "https://kauth.kakao.com/oauth/authorize?scope=talk_message,profile_nickname,account_email&response_type=code&redirect_uri="
                + kakaoProperties.redirectUrl() + "&client_id=" + kakaoProperties.clientId();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(kakaoAuthUrl));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/callback")
    public ResponseEntity<MemberResponse> kakaoCallback(@RequestParam("code") String code) {
        KakaoTokenResponse tokenResponse = kakaoOAuthService.getAccessToken(code);
        KakaoUserResponse userResponse = kakaoOAuthService.getUserInfo(tokenResponse.accessToken());

        MemberResponse memberResponse = kakaoOAuthService.registerOrLoginKakaoUser(userResponse);

        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/unlink/{accessToken}")
    public ResponseEntity<KakaoUnlinkResponse> unlink(@PathVariable String accessToken) {
        KakaoUnlinkResponse response = kakaoOAuthService.unlinkUser(accessToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/scopes/{accessToken}")
    public ResponseEntity<KakaoScopeResponse> getUserScopes(@PathVariable String accessToken) {
        KakaoScopeResponse response = kakaoOAuthService.getUserScopes(accessToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/userinfo/{accessToken}")
    public ResponseEntity<KakaoUserResponse> getUserInfo(@PathVariable String accessToken) {
        KakaoUserResponse response = kakaoOAuthService.getUserInfo(accessToken);
        return ResponseEntity.ok(response);
    }
}
