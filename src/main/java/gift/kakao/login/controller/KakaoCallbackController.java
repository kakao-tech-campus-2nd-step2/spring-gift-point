package gift.kakao.login.controller;

import gift.kakao.login.service.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoCallbackController {

    private final KakaoLoginService kakaoLoginService;

    public KakaoCallbackController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping
    public ResponseEntity<String> handleOAuthCallback(
        @RequestParam(value = "code") String code) {
        String jwtAccessToken = kakaoLoginService.getAccessToken(code);
        String kakaoEmail = kakaoLoginService.getUserInfo(jwtAccessToken);
        return new ResponseEntity<>(jwtAccessToken, HttpStatus.OK);
    }

}
