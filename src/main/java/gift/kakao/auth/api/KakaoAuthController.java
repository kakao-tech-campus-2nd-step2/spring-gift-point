package gift.kakao.auth.api;

import gift.auth.application.AuthService;
import gift.kakao.vo.KakaoProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoAuthController {

    private final AuthService authService;
    private final KakaoProperties kakaoProperties;

    public KakaoAuthController(AuthService authService,
                               KakaoProperties kakaoProperties) {
        this.authService = authService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping
    public String redirectKakaoLogin() {
        return "redirect:" + kakaoProperties.getKakaoAuthUrl();
    }

    @GetMapping("/callback")
    public String handleKakaoCallback(RedirectAttributes redirectAttributes,
                                      @RequestParam("code") String code) {
        redirectAttributes.addFlashAttribute("tokenResponse", authService.authenticate(code));
        return "redirect:/members/order";
    }

}
