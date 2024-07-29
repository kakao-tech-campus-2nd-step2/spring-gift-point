package gift.controller.oauth;

import gift.common.properties.KakaoProperties;
import gift.common.util.KakaoUtil;
import gift.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth", description = "OAuth API")
@RestController
@RequestMapping("/api/v1/kakao/login")
public class OAuthController {

    private final KakaoProperties kakaoProperties;
    private final OAuthService OAuthService;
    private final KakaoUtil kakaoUtil;

    public OAuthController(KakaoProperties kakaoProperties, OAuthService OAuthService,
        KakaoUtil kakaoUtil) {
        this.kakaoProperties = kakaoProperties;
        this.OAuthService = OAuthService;
        this.kakaoUtil = kakaoUtil;
    }

    @GetMapping("")
    @Operation(summary = "카카오 로그인 리다이렉션", description = "카카오 인가 코드를 발급받습니다.")
    public ResponseEntity<Void> login() {
        String loginUrl = kakaoUtil.getRequestUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", loginUrl);

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/callback")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 시도합니다.")
    public ResponseEntity<String> registerUser(@RequestParam("code") String code) {
        String token = OAuthService.register(code);
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).body(token);
    }
}
