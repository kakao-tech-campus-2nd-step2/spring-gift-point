package gift.controller;

import gift.service.KakaoAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoController {
    private static final Logger logger = LoggerFactory.getLogger(KakaoController.class);

    private final KakaoAuthService kakaoAuthService;

    public KakaoController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/oauth/kakao/callback")
    public String getKakaoAuthToken(@RequestParam String code) {
        String token = kakaoAuthService.getKakaoToken(code);
        logger.info("kakaoAuth return : {}", token);
        return token;
    }
}