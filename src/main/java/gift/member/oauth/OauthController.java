package gift.member.oauth;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Oauth API", description = "Oauth를 통해 Member를 만드는 API")
@RestController
@RequestMapping("/oauth/kakao/login")
public class OauthController {

    private final OauthService oauthService;

    public OauthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @Operation(summary = "KakaoOauth 로그인", description = "kakaoOauth 로그인한 후, token을 반환합니다.")
    @GetMapping
    public ResponseEntity<Void> getAuthorization() {
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
            .location(oauthService.getKakaoAuthorization()).build();
    }

    @Hidden
    @GetMapping("/callback")
    public ResponseEntity<String> getKakaoToken(@RequestParam("code") String authorizationCode) {
        String accessToken = oauthService.loginByKakao(authorizationCode);
        return ResponseEntity.ok(accessToken);
    }
}
