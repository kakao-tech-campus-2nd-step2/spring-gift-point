package gift.controller;

import gift.domain.model.dto.KakaoTokenResponseDto;
import gift.domain.model.dto.TokenResponseDto;
import gift.service.KakaoLoginService;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Tag(name = "Kakao Login", description = "카카오 로그인 API")
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final UserService userService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService, UserService userService) {
        this.kakaoLoginService = kakaoLoginService;
        this.userService = userService;
    }

    @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 후 콜백을 처리하고 토큰을 반환합니다.")
    @GetMapping
    public ResponseEntity<TokenResponseDto> callback(@RequestParam("code") String code) {
        KakaoTokenResponseDto Tokens = kakaoLoginService.getTokensFromKakao(code);
        TokenResponseDto tokenResponse = userService.loginOrRegisterKakaoUser(Tokens);
        return ResponseEntity.ok(tokenResponse);
    }
}