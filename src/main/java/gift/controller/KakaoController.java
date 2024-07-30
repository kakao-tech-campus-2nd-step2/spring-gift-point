package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.config.KakaoProperties;
import gift.model.BearerToken;
import gift.service.KakaoAuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class KakaoController {
    private static final Logger logger = LoggerFactory.getLogger(KakaoController.class);

    private final KakaoAuthService kakaoAuthService;

    public KakaoController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/oauth/kakao/callback")
    public String getKakaoAuthToken(@RequestParam String code){
        String token = kakaoAuthService.getKakaoToken(code);
        logger.info("kakaoAuth return : {}", token);
        return token;
    }
}
