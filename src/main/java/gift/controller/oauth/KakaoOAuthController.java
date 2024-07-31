package gift.controller.oauth;

import static gift.util.constants.auth.KakaoOAuthConstants.KAKAO_AUTH_URL;

import gift.config.KakaoProperties;
import gift.dto.member.MemberOAuthResponse;
import gift.dto.oauth.KakaoScopeResponse;
import gift.dto.oauth.KakaoTokenResponse;
import gift.dto.oauth.KakaoUnlinkResponse;
import gift.dto.oauth.KakaoUserResponse;
import gift.service.MemberService;
import gift.service.oauth.KakaoOAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Kakao OAuth API", description = "카카오 OAuth API")
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

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 페이지로 리디렉션합니다.")
    @GetMapping
    public ResponseEntity<Void> kakaoLogin() {
        String kakaoAuthUrl =
            KAKAO_AUTH_URL + "?scope=talk_message,profile_nickname,account_email&response_type=code&redirect_uri="
                + kakaoProperties.redirectUrl() + "&client_id=" + kakaoProperties.clientId();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(kakaoAuthUrl));
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).headers(headers).build();
    }

    @Operation(summary = "카카오 콜백", description = "카카오 로그인 콜백을 처리하고 JWT 토큰을 반환합니다.")
    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam("code") String code) {
        KakaoTokenResponse tokenResponse = kakaoOAuthService.getAccessToken(code);
        KakaoUserResponse userResponse = kakaoOAuthService.getUserInfo(tokenResponse.accessToken());
        MemberOAuthResponse memberOAuthResponse = kakaoOAuthService.registerOrLoginKakaoUser(userResponse);

        kakaoOAuthService.saveToken(tokenResponse, memberOAuthResponse.id());

        String jwt = kakaoOAuthService.generateJwt(memberOAuthResponse.id(), memberOAuthResponse.email());

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 권한 조회", description = "카카오 사용자 권한을 조회합니다.")
    @GetMapping("/scopes")
    public ResponseEntity<KakaoScopeResponse> getUserScopes(@RequestAttribute("memberId") Long memberId) {
        KakaoScopeResponse response = kakaoOAuthService.getUserScopes(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 정보 조회", description = "카카오 사용자 정보를 조회합니다.")
    @GetMapping("/userinfo")
    public ResponseEntity<KakaoUserResponse> getUserInfo(@RequestAttribute("memberId") Long memberId) {
        KakaoUserResponse response = kakaoOAuthService.getUserInfo(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카카오 연결 해제", description = "카카오 사용자 연결을 해제하고 회원 정보를 삭제합니다.")
    @DeleteMapping("/unlink")
    public ResponseEntity<KakaoUnlinkResponse> unlink(@RequestAttribute("memberId") Long memberId) {
        kakaoOAuthService.unlinkUser(memberId);
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
