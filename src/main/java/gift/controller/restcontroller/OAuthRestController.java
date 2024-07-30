package gift.controller.restcontroller;

import gift.common.annotation.LoginMember;
import gift.controller.dto.response.TokenResponse;
import gift.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OAuth", description = "OAuth Rest API")
@RestController
@RequestMapping("/api/oauth")
public class OAuthRestController {

    private final OAuthService oAuthService;

    public OAuthRestController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/kakao/login/callback")
    @Operation(summary = "카카오 로그인 리다이렉션", description = "카카오 액세스 토큰을 발급받습니다.")
    public ResponseEntity<TokenResponse> kakaoToken(@RequestParam("code") @NotNull String code) {
        TokenResponse response = oAuthService.signIn(code);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/kakao/logout")
    @Operation(summary = "카카오 로그아웃", description = "카카오에서 로그아웃합니다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Void> kakaoLogOut(
            @Parameter(hidden = true) @NotNull @LoginMember Long memberId) {
        oAuthService.signOut(memberId);
        return ResponseEntity.ok().build();
    }
}
