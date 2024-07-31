package gift.controller.member;

import gift.application.member.MemberFacade;
import gift.application.member.dto.OAuthCommand;
import gift.controller.member.dto.MemberResponse;
import gift.controller.member.dto.MemberResponse.Login;
import gift.controller.member.dto.OAuthRequest;
import gift.global.config.KakaoProperties;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

    private final MemberFacade memberFacade;
    private final KakaoProperties kakaoProperties;

    public OAuthController(MemberFacade memberFacade, KakaoProperties kakaoProperties) {
        this.memberFacade = memberFacade;
        this.kakaoProperties = kakaoProperties;
    }

    @Operation(summary = "소셜 로그인", description = "소셜 로그인 api")
    @GetMapping("/api/oauth/kakao/login/callback")
    public ResponseEntity<MemberResponse.Login> login(
        @RequestParam("code") String code
    ) {
        var response = memberFacade.socialLogin(new OAuthCommand.Login(code));
        return ResponseEntity.status(HttpStatus.OK)
            .header("Authorization", response.jwt())
            .body(Login.from(response));

    }

    @GetMapping("/api/oauth/kakao/login")
    public ResponseEntity<Void> getOauthURL() {
        var kakaoLoginUrl = kakaoProperties.getKakaoLoginUrl();
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
            .header("location", kakaoLoginUrl)
            .build();
    }
}
