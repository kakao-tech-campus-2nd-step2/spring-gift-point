package gift.controller.auth;

import gift.service.KakaoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private final KakaoService kakaoService;

    public AuthController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("login/oauth/kakao")
    public String getKakaoAuthorizationCode(){
        String param = kakaoService.makeKakaOauthParameter().toString();
        return "redirect:https://kauth.kakao.com/oauth/authorize?" + param;
    }
}
