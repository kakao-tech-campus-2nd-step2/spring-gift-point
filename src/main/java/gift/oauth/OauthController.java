package gift.oauth;

import static org.springframework.http.HttpStatus.FOUND;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Oauth", description = "Oauth API")
@RestController
@RequestMapping("/api/oauth")
public class OauthController {

    private final KakaoOauthService oauthService;

    public OauthController(KakaoOauthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/kakao/login")
    @Operation(summary = "카카오톡 로그인", description = "카카오톡 로그인 URL로 이동합니다.")
    @ApiResponse(responseCode = "302", description = "URL 반환 성공")
    public ResponseEntity<Void> kakaoLogin() {
        return ResponseEntity.status(FOUND)
            .location(oauthService.getLoginURL())
            .build();
    }

    @GetMapping("/kakao/token")
    @Operation(summary = "카카오톡 액세스 토큰 발급", description = "인가 코드를 통해 카카오톡 액세스 토큰을 발급 받습니다.")
    @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
    public String getToken(@RequestParam String code) {
        return oauthService.getAccessToken(code);
    }
}
