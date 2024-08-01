package gift.controller;

import gift.entity.Member;
import gift.exception.MemberNotFoundException;
import gift.service.KakaoService;
import gift.util.KakaoProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/kakao")
@Tag(name = "Kakao Login API", description = "카카오 로그인 관련 API")
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    @Autowired
    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login")
    @Operation(summary = "카카오 로그인 페이지", description = "카카오 로그인 페이지를 반환합니다.")
    public String login(Model model) {
        model.addAttribute("kakaoClientId", kakaoProperties.clientId());
        model.addAttribute("kakaoRedirectUrl", kakaoProperties.redirectUrl());
        return "kakaoLogin";
    }

    @GetMapping("/oauth2/callback")
    @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 콜백을 처리합니다.")
    public String callbackKakao(@RequestParam String code, Model model) {
        try {
            String accessToken = kakaoService.login(code);
            return "home";
        } catch (MemberNotFoundException e) {
            model.addAttribute("member", new Member());
            return "register";
        }
    }

    @GetMapping("/loginSuccess")
    @Operation(summary = "카카오 로그인 성공 페이지", description = "카카오 로그인 성공 페이지를 반환합니다.")
    public String loginSuccess() {
        return "kakaoLoginSuccess";
    }
}
