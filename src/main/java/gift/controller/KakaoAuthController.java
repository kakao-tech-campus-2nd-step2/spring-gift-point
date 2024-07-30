package gift.controller;

import gift.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "Kakao Auth API", description = "Kakao 로그인 관련 API")
@RestController
@RequestMapping("/api/kakao")
public class KakaoAuthController {
    private KakaoService kakaoService;
    public KakaoAuthController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @Operation(summary = "카카오 로그인 URL", description = "카카오 로그인을 위한 URL을 생성한다.")
    @GetMapping("/login")
    public RedirectView getKakaoLoginUrl() {
        RedirectView redirectView = new RedirectView();
        String url = kakaoService.getKakaoUrl();
        redirectView.setUrl(url);
        return redirectView;
    }

    @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 후 콜백을 처리한다.")
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessToken(code);
        return ResponseEntity.ok(accessToken);
    }
}
