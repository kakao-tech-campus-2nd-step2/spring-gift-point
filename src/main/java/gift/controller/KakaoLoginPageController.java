package gift.controller;

import gift.config.KakaoAuthProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/kakao/login")
public class KakaoLoginPageController {
    private final KakaoAuthProperties kakaoAuthProperties;

    public KakaoLoginPageController(KakaoAuthProperties kakaoAuthProperties) {
        this.kakaoAuthProperties = kakaoAuthProperties;
    }

    @GetMapping("/page")
    public String loginPage() {
        return "kakao-login";
    }

    @GetMapping("/redirect")
    public RedirectView redirectLoginPage() {
        String location = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
            .queryParam("response_type", "code")
            .queryParam("client_id", kakaoAuthProperties.getClientId())
            .queryParam("redirect_uri", kakaoAuthProperties.getRedirectUri())
            .toUriString();

        return new RedirectView(location);
    }
}
