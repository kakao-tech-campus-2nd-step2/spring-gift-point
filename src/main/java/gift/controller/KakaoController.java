package gift.controller;

import gift.config.KakaoProperties;
import gift.dto.SuccessResponse;
import gift.dto.KakaoToken;
import gift.service.KakaoOAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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
    public ResponseEntity<Object> getTokenAndUserInfo(
            @RequestParam(value = "code") String kakaoCode
    ) {
        String returnUrl = "http://localhost:3000/token.html?tokenValue=";
        KakaoToken kakaoToken = kakaoOAuthService.getKakaoToken(kakaoCode);
        String token = kakaoOAuthService.kakaoMemberRegister(kakaoToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(returnUrl + token));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/unlink")
    public ResponseEntity<SuccessResponse> logout(@RequestHeader("Authorization") String token) {
        kakaoOAuthService.unlink(token);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.NO_CONTENT, "계정 연결해제 성공적"));
    }
}
