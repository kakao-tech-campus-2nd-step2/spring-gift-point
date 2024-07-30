package gift.controller;

import gift.service.KakaoLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/api/kakao/login")
    public String KakaoLogin() {
        return "redirect:" + kakaoLoginService.getUrl();
    }

    @GetMapping(params = "code")
    public ResponseEntity<String> signUpAndLogin(@RequestParam String code) {
        String Token = kakaoLoginService.getAccessToken(code);
        String message = kakaoLoginService.signUpAndLogin(Token);
        return ResponseEntity.ok().body(message);
    }
}
