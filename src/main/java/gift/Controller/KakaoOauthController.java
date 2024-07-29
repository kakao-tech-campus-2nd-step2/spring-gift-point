package gift.Controller;

import gift.Service.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@Tag(name = "카카오 소셜 로그인", description = "카카오 소셜 로그인 API")
@RequestMapping("/members")
@RestController
public class KakaoOauthController {

    private KakaoLoginService kakaoLoginService;

    public KakaoOauthController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }


    @Operation(summary = "카카오 소셜 로그인", description = "카카오 소셜 로그인을 요청합니다")
    @ApiResponse(responseCode = "302", description = "소셜 로그인 요청 완료")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @GetMapping("/kakao-login")
    public RedirectView requestLogin(){
        URI redirectURI = kakaoLoginService.requestLogin();
        return new RedirectView(redirectURI.toString());
    }

    @Operation(summary = "액세스 토큰 발급", description = "액세스 토큰을 발급합니다")
    @ApiResponse(responseCode = "200", description = "발급 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @GetMapping("/login")
    public ResponseEntity<String> loginOrRegisterUser(@RequestParam ("code") String oauthCode) {
        String token = kakaoLoginService.loginOrRegisterUser(oauthCode);
        return ResponseEntity.ok(token);
    }
}
