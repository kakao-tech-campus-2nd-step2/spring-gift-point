package gift.user.presentation;

import gift.user.application.KakaoOauthService;
import gift.util.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "KakaoOauthController", description = "카카오 OAuth 관련 API")
@RestController
public class KakaoOauthController {

    private final KakaoOauthService kakaoOauthService;

    public KakaoOauthController(KakaoOauthService kakaoOauthService) {
        this.kakaoOauthService = kakaoOauthService;
    }

    @Operation(summary = "로그인 폼", description = "카카오 로그인 URL을 생성합니다.")
    @GetMapping("/login")
    public ResponseEntity<?> loginForm() {
        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(
                        kakaoOauthService.getKakaoLoginUrl(), "카카오 로그인 URL 생성 성공", true
                ));
    }

    @Operation(summary = "로그아웃", description = "카카오 로그아웃을 처리합니다.")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@Parameter(description = "액세스 토큰") @RequestParam String accessToken) {
        kakaoOauthService.getKakaoLogout(accessToken);
        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(null, "로그아웃 성공", true));
    }

    @Operation(summary = "카카오 OAuth 콜백", description = "카카오 로그인 콜백을 처리합니다.")
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> kakaoOauthCallback(@Parameter(description = "코드") @RequestParam String code) {
        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(
                        kakaoOauthService.processKakaoLoginAndGenerateAccessToken(code), "카카오 로그인 성공", true
                ));
    }

    @Operation(summary = "카카오 로그인 확인", description = "카카오 로그인을 확인합니다.")
    @PostMapping("/login/ok")
    public ResponseEntity<?> loginOk(@Parameter(description = "코드") @RequestParam String code) {
        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(
                        kakaoOauthService.processKakaoLoginAndGenerateAccessToken(code), "카카오 로그인 성공", true
                ));
    }
}
