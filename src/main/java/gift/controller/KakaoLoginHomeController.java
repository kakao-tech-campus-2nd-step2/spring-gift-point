package gift.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kakao/login/home")
public class KakaoLoginHomeController {
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.scope}")
    private String scope;

    @GetMapping
    @Operation(summary = "카카오 로그인 페이지", description = "카카오 로그인 페이지로 이동한다.")
    public String kakaoLoginPage(Model model) {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope;
        model.addAttribute("kakaoAuthUrl", kakaoAuthUrl);
        return "kakao-login-home";
    }
}
