package gift.doamin.user.controller;

import gift.doamin.user.dto.KakaoOAuthTokenResponse;
import gift.doamin.user.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Tag(name = "OAuth", description = "카카오 로그인 관련 API")
@RestController
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }


    @Operation(summary = "카카오 로그인", description = "카카오 인증 페이지로 리다이렉션되며 카카오 로그인 절차가 수행됩니다.")
    @ApiResponse(responseCode = "302", description = "카카오 인증 페이지로 리다이렉션", content = @Content)
    @GetMapping("/oauth2/authorization/kakao")
    public ModelAndView kakaoLogin(HttpServletResponse response) {

        return new ModelAndView("redirect:" + oAuthService.getAuthUrl());
    }

    @Operation(summary = "OAuth redirection uri", description = "인증 결과 카카오 클라이언트에서 리다이렉션 될 uri입니다. 카카오 로그인에 성공했다면 우리 클라이언트에 refresh토큰을 설정합니다.")
    @GetMapping("login/oauth2/code/kakao")
    public ResponseEntity<Void> kakaoLogin(@RequestParam(name = "code") String authorizeCode) {
        KakaoOAuthTokenResponse kakaoOAuthToken = oAuthService.requestToken(authorizeCode);
        String myRefreshToken = oAuthService.authenticate(kakaoOAuthToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", myRefreshToken)
            .httpOnly(true)
            .maxAge(12 * 60 * 60)
            .path("/api/auth/accessToken")
            .build();
        return ResponseEntity.ok()
            .header("Set-Cookie", cookie.toString())
            .build();
    }

}
