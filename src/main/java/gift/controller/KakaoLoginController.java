package gift.controller;


import gift.config.KakaoProperties;
import gift.model.Member;
import gift.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
@Tag(name = "카카오 로그인 API", description = "카카오 로그인을 위한 API")
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakao/auth")
    @Operation(summary = "카카오 로그인 페이지로 이동", description = "카카오 로그인 페이지로 이동합니다.")
    public String kakaoLogin(Model model) {
        String loginUrl = kakaoService.generateKakaoLoginUrl();
        return "redirect:" + loginUrl;
    }

    @GetMapping
    @Operation(summary = "카카오 액세스 토큰 획득", description = "카카오 액세스 토큰을 획득합니다.")
    public String kakaoAccessToken(@RequestParam(value = "code") String authorizationCode,
        RedirectAttributes redirectAttributes) {
        if (authorizationCode != null) {
            String accessToken = kakaoService.getAccessToken(authorizationCode);
            String email = kakaoService.getUserEmail(accessToken);
            Member member = kakaoService.saveKakaoUser(email);
            String jwtToken = kakaoService.generateToken(member.getEmail(), member.getRole());
            redirectAttributes.addFlashAttribute("accessToken", accessToken);
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("jwtToken", jwtToken);
            return "redirect:/kakao/success";
        }
        String loginUrl = kakaoService.generateKakaoLoginUrl();
        return "redirect:" + loginUrl;
    }

    @GetMapping("/kakao/success")
    @Operation(summary = "카카오 로그인 성공 페이지로 이동", description = "카카오 로그인 성공 페이지로 이동합니다.")
    public String kakaoAcessSuccess(Model model) {
        return "kakao_access_token";
    }
}