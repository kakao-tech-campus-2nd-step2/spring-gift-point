package gift.controller;

import gift.config.KakaoProperties;
import gift.dto.KakaoTokenResponse;
import gift.dto.KakaoUserResponse;
import gift.service.KakaoUserService;
import gift.service.TokenService;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Tag(name = "Kakao User Management", description = "APIs for managing Kakao users")
public class KakaoUserController {

    private final KakaoUserService kakaoUserService;
    private final UserService userService;
    private final KakaoProperties kakaoProperties;
    private final TokenService tokenService;

    public KakaoUserController(KakaoUserService kakaoUserService, UserService userService,
        KakaoProperties kakaoProperties, TokenService tokenService) {
        this.kakaoUserService = kakaoUserService;
        this.userService = userService;
        this.kakaoProperties = kakaoProperties;
        this.tokenService = tokenService;
    }

    @GetMapping("/oauth/kakao")
    @Operation(summary = "카카오 인증 리다이렉트", description = "카카오 인증 페이지로 리다이렉트합니다.\n이 엔드포인트는 Swagger UI에서 직접 호출할 수 없습니다. 브라우저를 통해 직접 접근해야 합니다.",
        responses = @ApiResponse(responseCode = "302", description = "카카오 인증 리다이렉트 성공"))
    public RedirectView redirectToAuthorization() {
        String url = kakaoUserService.getAuthorizationUrl();
        return new RedirectView(url);
    }

    @GetMapping("/oauth/kakao/callback")
    @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 콜백을 처리합니다.",
        responses = @ApiResponse(responseCode = "200", description = "카카오 로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = KakaoTokenResponse.class))))
    public ResponseEntity<Void> kakaoCallback(@RequestParam String code) {
        KakaoTokenResponse response = kakaoUserService.getAccessToken(code);
        String userEmail = kakaoUserService.getUserInfo(
            response.accessToken()).kakaoAccount().email();

        userService.kakaoUserRegister(userEmail,
            kakaoProperties.getDefaultPassword());

        userService.setAccessToken(response.accessToken(), userEmail);

        String jwt = tokenService.generateToken(userEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
            URI.create(kakaoProperties.getFrontRedirectUri() + "?tokenValue=" + jwt));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/kakao/userinfo")
    @Operation(summary = "카카오 사용자 정보 조회", description = "카카오 사용자 정보를 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "카카오 사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = KakaoUserResponse.class))))
    public ResponseEntity<KakaoUserResponse> kakaoUserInfo(@RequestParam String accessToken) {
        return ResponseEntity.ok().body(kakaoUserService.getUserInfo(accessToken));
    }
}
