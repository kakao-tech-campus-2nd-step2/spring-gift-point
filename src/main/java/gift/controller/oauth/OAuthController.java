package gift.controller.oauth;

import gift.common.util.KakaoUtil;
import gift.controller.user.dto.UserResponse;
import gift.service.OAuthService;
import gift.service.UserDto;
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
@RequestMapping("/api/oauth/kakao/login")
public class OAuthController {

    private final OAuthService OAuthService;
    private final KakaoUtil kakaoUtil;

    public OAuthController(OAuthService OAuthService, KakaoUtil kakaoUtil) {
        this.OAuthService = OAuthService;
        this.kakaoUtil = kakaoUtil;
    }

    @GetMapping("")
    @Operation(summary = "카카오 로그인 리다이렉션", description = "카카오 인가 코드를 발급받습니다.")
    public ResponseEntity<Void> login(
        @RequestParam("redirect-url") String redirectUrl
    ) {
        String loginUrl = kakaoUtil.getRequestUrl(redirectUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", loginUrl);

        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/callback")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 시도합니다.")
    public ResponseEntity<UserResponse.Login> registerUser(
        @RequestParam("code") String code,
        @RequestParam("redirect-url") String redirectUrl
    ) {
        UserDto response = OAuthService.register(code, redirectUrl);
        return ResponseEntity.ok()
            .header("Authorization", response.accessToken())
            .body(UserResponse.Login.from(response.name(), response.role()));
    }
}
