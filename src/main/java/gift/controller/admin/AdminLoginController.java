package gift.controller.admin;

import gift.common.properties.KakaoProperties;
import gift.controller.dto.request.SignInRequest;
import gift.controller.dto.response.TokenResponse;
import gift.service.AuthService;
import gift.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminLoginController {

    private final KakaoProperties properties;
    private final AuthService authService;
    private final OAuthService oAuthService;

    public AdminLoginController(AuthService authService, OAuthService oAuthService, KakaoProperties properties) {
        this.authService = authService;
        this.oAuthService = oAuthService;
        this.properties = properties;
    }

    @GetMapping("")
    public String loginPage() {
        return "login";
    }

    @GetMapping("kakao")
    public String kakaoLoginPage() {
        return "redirect:" + properties.adminLoginUrl() + properties.clientId();
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute SignInRequest request, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.signIn(request);
        String token = tokenResponse.accessToken();
        ResponseCookie cookie = ResponseCookie.from("Authorization", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60) // 24시간 유효기간
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return "redirect:/admin";
    }

    @GetMapping("kakao/login/callback")
    public String kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {
        TokenResponse tokenResponse = oAuthService.signIn(code, properties.adminRedirectUrl());
        String token = tokenResponse.accessToken();
        ResponseCookie cookie = ResponseCookie.from("Authorization", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60) // 24시간 유효기간
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return "redirect:/admin";
    }
}
