package gift.controller.oauth;

import gift.config.KakaoProperties;
import gift.dto.member.MemberResponse;
import gift.dto.oauth.KakaoScopeResponse;
import gift.dto.oauth.KakaoTokenResponse;
import gift.dto.oauth.KakaoUnlinkResponse;
import gift.dto.oauth.KakaoUserResponse;
import gift.service.MemberService;
import gift.service.oauth.KakaoOAuthService;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoOAuthController {

    private final KakaoProperties kakaoProperties;
    private final KakaoOAuthService kakaoOAuthService;
    private final MemberService memberService;

    public KakaoOAuthController(
        KakaoProperties kakaoProperties,
        KakaoOAuthService kakaoOAuthService,
        MemberService memberService
    ) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoOAuthService = kakaoOAuthService;
        this.memberService = memberService;
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
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam("code") String code) {
        KakaoTokenResponse tokenResponse = kakaoOAuthService.getAccessToken(code);
        KakaoUserResponse userResponse = kakaoOAuthService.getUserInfo(tokenResponse.accessToken());
        MemberResponse memberResponse = kakaoOAuthService.registerOrLoginKakaoUser(userResponse);

        kakaoOAuthService.saveToken(tokenResponse, memberResponse.id());

        String jwt = kakaoOAuthService.generateJwt(memberResponse.id(), memberResponse.email());

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/scopes")
    public ResponseEntity<KakaoScopeResponse> getUserScopes(@RequestAttribute("memberId") Long memberId) {
        KakaoScopeResponse response = kakaoOAuthService.getUserScopes(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<KakaoUserResponse> getUserInfo(@RequestAttribute("memberId") Long memberId) {
        KakaoUserResponse response = kakaoOAuthService.getUserInfo(memberId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/unlink")
    public ResponseEntity<KakaoUnlinkResponse> unlink(@RequestAttribute("memberId") Long memberId) {
        kakaoOAuthService.unlinkUser(memberId);
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
