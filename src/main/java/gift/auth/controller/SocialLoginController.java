package gift.auth.controller;

import gift.auth.service.SocialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "소셜 로그인 폼 호출 서비스")
@Controller
@RequestMapping("/api/social")
public class SocialLoginController {
    private final SocialService socialService;

    @Autowired
    public SocialLoginController(SocialService socialService) {
        this.socialService = socialService;
    }

    @Operation(summary  = "카카오 코드로 카카오 코드를 받아온다.", description = "Rest방식이 아닙니다 주의하세요, 요청시 카카오 로그인 뷰를 반환합니다.")
    @GetMapping("/code/kakao")
    public String GetKakaoCode() {
        return "redirect:" + socialService.getKakoCode();
    }
}
