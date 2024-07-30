package gift.controller;

import gift.dto.KakaoUserProfile;
import gift.service.KakaoOAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "카카오 OAuth API", description = "카카오 OAuth 관련 API")
@Controller
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoService;

    public KakaoOAuthController(KakaoOAuthService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @Operation(summary = "카카오 OAuth 콜백", description = "카카오 OAuth 인증 후 콜백을 처리합니다.")
    @GetMapping("/oauth/kakao/callback")
    public String kakaoCallback(@RequestParam String code) {
        String accessToken = kakaoService.getAccessToken(code);
        KakaoUserProfile userProfile = kakaoService.getUserProfile(accessToken);
        return "redirect:/";
    }
}
