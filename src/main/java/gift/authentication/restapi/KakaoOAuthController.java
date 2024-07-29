package gift.authentication.restapi;

import gift.advice.ErrorResponse;
import gift.authentication.restapi.dto.response.LoginResponse;
import gift.core.domain.authentication.OAuthService;
import gift.core.domain.authentication.OAuthType;
import gift.core.domain.authentication.Token;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "OAuth")
public class KakaoOAuthController {
    private final OAuthService oAuthService;

    public KakaoOAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/api/oauth/kakao")
    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인을 수행합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "카카오 로그인 성공 시 액세스 토큰을 생성하여 반환합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "카카오 메시지 전달 실패 시 반환합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public LoginResponse login(@RequestParam("code") String code) {
        Token token = oAuthService.authenticate(OAuthType.KAKAO, code);

        return LoginResponse.of(token);
    }
}
