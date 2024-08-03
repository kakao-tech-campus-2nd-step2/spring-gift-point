package gift.controller;

import gift.common.exception.unauthorized.TokenNotFoundException;
import gift.dto.KakaoAccessToken;
import gift.dto.KakaoProperties;
import gift.dto.KakaoUserInfo;
import gift.service.KakaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/kakao")
public class KakaoController {

    private final KakaoService kakaoService;
    private final KakaoProperties kakaoProperties;

    public KakaoController(KakaoService kakaoService, KakaoProperties kakaoProperties) {
        this.kakaoService = kakaoService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("kakaoClientId", kakaoProperties.getClientId());
        model.addAttribute("kakaoRedirectUrl", kakaoProperties.getRedirectUrl());
        return "login";
    }

    @GetMapping("/direct/login")
    public String loginForm() {
        return "redirect:" + kakaoService.getKakaoLogin();
    }

    @GetMapping("/")
    public String kakaoCallback(@RequestParam(required = false) String code, Model model,
        HttpSession session) {
        if (code == null) {
            throw new TokenNotFoundException();
        }

        KakaoAccessToken tokenResponse = kakaoService.getAccessToken(code);
        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new TokenNotFoundException();
        }
        String accessToken = tokenResponse.getAccessToken();

        KakaoUserInfo userInfo = kakaoService.getUserInfo(accessToken);

        session.setAttribute("userInfo", userInfo);
        session.setAttribute("accessToken", accessToken);

        System.out.println("Access Token: " + accessToken);

        return "redirect:/user";
    }

    @GetMapping("/user")
    public String user(Model model, @SessionAttribute("userInfo") KakaoUserInfo userInfo) {
        if (userInfo == null) {
            throw new RuntimeException("사용자 정보를 찾을 수 없습니다.");
        }

        model.addAttribute("userInfo", userInfo);
        return "user";
    }

}
