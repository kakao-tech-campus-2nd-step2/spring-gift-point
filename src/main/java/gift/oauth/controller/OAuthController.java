package gift.oauth.controller;

import gift.kakaoApi.exception.KakaoLoginException;
import gift.kakaoApi.service.KakaoApiService;
import gift.oauth.service.OAuthService;
import gift.util.dto.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
@Tag(name = "OAuthController", description = "OAuthController API")
public class OAuthController {

    private final OAuthService oauthService;
    private final KakaoApiService kakaoApiService;

    public OAuthController(OAuthService oauthService, KakaoApiService kakaoApiService) {
        this.oauthService = oauthService;
        this.kakaoApiService = kakaoApiService;
    }


    @Operation(summary = "카카오 로그인", description = "카카오 OAuth로 로그인을 시도합니다.")
    @ApiResponse(responseCode = "302", description = "리디렉션이 발생합니다. 카카오 로그인 페이지로 이동합니다.")
    @GetMapping("/login")
    public void loginKakao(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoApiService.getKakaoLoginUri());
    }

    @GetMapping("/token")
    @Operation(summary = "카카오 토큰 발급", description = "카카오 API를 통해 서버 엑세스 토큰을 발급합니다.")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<JwtResponse> getAccessToken(
        @RequestParam(required = false) String code,
        @RequestParam(required = false) String error,
        @RequestParam(required = false) String error_description
    ) {
        if (error != null || error_description != null) {
            throw new KakaoLoginException("카카오 인증 실패");
        }
        return ResponseEntity.ok(new JwtResponse(oauthService.getAccessToken(code)));
    }
}
