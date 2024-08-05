package gift.member.controller;

import gift.member.KakaoProperties;
import gift.member.dto.MemberResponse;
import gift.member.service.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Kakao Login", description = "카카오 로그인 관련 API")
@RestController
@RequestMapping("/api/members/kakao")
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoLoginService kakaoLoginService;

    private static final String KAKAO_OAUTH_CODE_URL = "https://kauth.kakao.com/oauth/authorize";

    @Autowired
    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoLoginService kakaoLoginService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoLoginService = kakaoLoginService;
    }

    @Operation(summary = "카카오 로그인", description = "카카오 아이디로 로그인을 합니다.")
    @GetMapping("/login")
    public ResponseEntity<Object> kakaoLogin(Model model) {
        String url = KAKAO_OAUTH_CODE_URL
            + "?client_id=" + kakaoProperties.clientId()
            + "&redirect_uri=" + kakaoProperties.redirectUri()
            + "&response_type=code";
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
            .location(URI.create(url))
            .build();
    }

    @Operation(hidden = true)
    @GetMapping("/oauth")
    public ResponseEntity<MemberResponse> kakaoCallback(@RequestParam("code") String code) {
        return ResponseEntity.ok().body(kakaoLoginService.login(code));
    }
}
