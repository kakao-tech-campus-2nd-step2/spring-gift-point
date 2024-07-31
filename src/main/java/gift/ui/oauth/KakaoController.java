package gift.ui.oauth;

import gift.api.member.enums.Scope;
import gift.global.config.KakaoProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/api/oauth")
public class KakaoController {

    private final KakaoProperties kakaoProperties;

    public KakaoController(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping
    public String requestLogin() {
        return "oauth";
    }

    @GetMapping("/kakao")
    public RedirectView requestAuthorizationCode() {
        return new RedirectView(
            String.format(kakaoProperties.url().requestFormat(),
                String.join(",", Scope.EMAIL.id(), Scope.MESSAGE.id()),
                kakaoProperties.url().redirect(),
                kakaoProperties.clientId()));
    }
}
