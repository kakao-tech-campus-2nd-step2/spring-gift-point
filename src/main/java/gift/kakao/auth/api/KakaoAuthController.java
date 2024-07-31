package gift.kakao.auth.api;

import gift.auth.application.AuthService;
import gift.auth.dto.AuthResponse;
import gift.kakao.auth.dto.KakaoUrlResponse;
import gift.kakao.vo.KakaoProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth/kakao")
public class KakaoAuthController {

    private final AuthService authService;
    private final KakaoProperties kakaoProperties;

    public KakaoAuthController(AuthService authService,
                               KakaoProperties kakaoProperties) {
        this.authService = authService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/url")
    public KakaoUrlResponse getRedirectKakaoLogin() {
        return new KakaoUrlResponse(kakaoProperties.getKakaoAuthUrl());
    }

    @GetMapping("/code")
    public AuthResponse handleKakaoLoginRedirect(@RequestParam("code") String code) {
        return authService.authenticate(code);
    }

}
