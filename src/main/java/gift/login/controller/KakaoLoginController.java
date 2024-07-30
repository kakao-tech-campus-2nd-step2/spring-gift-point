package gift.login.controller;

import gift.kakao.login.service.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "kakao 로그인")
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("")
    @Operation(summary = "kakao 로그인, Oauth callback 처리")
    public ResponseEntity<String> handleOAuthCallback(
        @RequestParam(value = "code") String code) {
        String jwtAccessToken = kakaoLoginService.getAccessToken(code);
        String kakaoEmail = kakaoLoginService.getUserInfo(jwtAccessToken);
        return new ResponseEntity<>(jwtAccessToken, HttpStatus.OK);
    }
}
