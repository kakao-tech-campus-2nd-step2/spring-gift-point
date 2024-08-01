package gift.controller;

import gift.config.KakaoProperties;
import gift.dto.SuccessResponse;
import gift.dto.KakaoToken;
import gift.dto.TokenResponseDto;
import gift.service.KakaoOAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "카카오 로그인 API", description = "카카오 로그인 관련 API")
@RestController
@RequestMapping("/oauth/kakao")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoOAuthService kakaoOAuthService;

    public KakaoController(KakaoProperties kakaoProperties, KakaoOAuthService kakaoOAuthService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoOAuthService = kakaoOAuthService;
    }

    @GetMapping
    public RedirectView kakaoLogin() {
        return new RedirectView(
                UriComponentsBuilder.newInstance()
                        .scheme("https")
                        .host("kauth.kakao.com")
                        .path("/oauth/authorize")
                        .queryParam("client_id", kakaoProperties.clientId())
                        .queryParam("redirect_uri", kakaoProperties.redirectURL())
                        .queryParam("response_type", "code")
                        .build(true).toString()
        );
    }

    @GetMapping("/redirect")
    public ResponseEntity<TokenResponseDto> getTokenAndUserInfo(
            @RequestParam(value = "code") String kakaoCode
    ) {
        KakaoToken kakaoToken = kakaoOAuthService.getKakaoToken(kakaoCode);
        TokenResponseDto token = kakaoOAuthService.kakaoMemberRegister(kakaoToken);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/unlink")
    public ResponseEntity<SuccessResponse> logout(@RequestHeader("Authorization") String token) {
        kakaoOAuthService.unlink(token);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.NO_CONTENT, "계정 연결해제 성공적"));
    }
}
