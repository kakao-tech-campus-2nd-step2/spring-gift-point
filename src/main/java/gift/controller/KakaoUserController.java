package gift.controller;

import gift.config.KakaoProperties;
import gift.dto.KakaoTokenResponse;
import gift.dto.KakaoUserResponse;
import gift.service.KakaoUserService;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    public KakaoUserController(KakaoUserService kakaoUserService, UserService userService,
        KakaoProperties kakaoProperties) {
        this.kakaoUserService = kakaoUserService;
        this.userService = userService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/kakao/auth")
    @Operation(summary = "카카오 인증 리다이렉트", description = "카카오 인증 페이지로 리다이렉트합니다.\n이 엔드포인트는 Swagger UI에서 직접 호출할 수 없습니다. 브라우저를 통해 직접 접근해야 합니다.",
        responses = @ApiResponse(responseCode = "302", description = "카카오 인증 리다이렉트 성공"))
    public RedirectView redirectToAuthorization() {
        String url = kakaoUserService.getAuthorizationUrl();
        return new RedirectView(url);
    }

    @GetMapping("/kakao/login")
    @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 콜백을 처리합니다.",
        responses = @ApiResponse(responseCode = "200", description = "카카오 로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = KakaoTokenResponse.class))))
    public ResponseEntity<KakaoTokenResponse> kakaoCallback(@RequestParam String code) {
        KakaoTokenResponse response = kakaoUserService.getAccessToken(code);

        kakaoProperties.setAccessToken(response.accessToken());

        KakaoUserResponse userResponse = kakaoUserService.getUserInfo(
            kakaoProperties.getAccessToken());
        userService.kakaoUserRegister(userResponse.kakaoAccount().email(),
            kakaoProperties.getDefaultPassword());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/kakao/userinfo")
    @Operation(summary = "카카오 사용자 정보 조회", description = "카카오 사용자 정보를 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "카카오 사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = KakaoUserResponse.class))))
    public ResponseEntity<KakaoUserResponse> kakaoUserInfo(@RequestParam String accessToken) {
        return ResponseEntity.ok().body(kakaoUserService.getUserInfo(accessToken));
    }
}
