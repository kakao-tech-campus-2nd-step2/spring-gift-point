package gift.controller;

import gift.service.KakaoAuthService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoAuthController {
    private final KakaoAuthService kakaoAuthService;
    private final JwtUtil jwtUtil;

    @Autowired
    KakaoAuthController(KakaoAuthService kakaoAuthService, JwtUtil jwtUtil) {
        this.kakaoAuthService = kakaoAuthService;
        this.jwtUtil = jwtUtil;
    }

    @Hidden
    @GetMapping
    public String loginOrRegister(@RequestParam String code) {
        return jwtUtil.generateToken(kakaoAuthService.loginOrRegister(code));
    }
}
