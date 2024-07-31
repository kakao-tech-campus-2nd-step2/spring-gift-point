package gift.users.kakao;

import gift.error.KakaoAuthenticationException;
import gift.response.ApiResponse;
import gift.response.ApiResponse.HttpResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
@Tag(name = "kakaoLogin API", description = "kakao login related API")
public class KakaoAuthApiController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthApiController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/authorize")
    @Operation(summary = "get kakao login page", description = "카카오 로그인 페이지를 가져옵니다.")
    public void kakaoLogin(HttpServletResponse response) {
        String kakaoLoginUrl = kakaoAuthService.getKakaoLoginUrl();
        try {
            response.sendRedirect(kakaoLoginUrl);
        } catch (IOException e) {
            throw new KakaoAuthenticationException("카카오 로그인에 실패했습니다.");
        }
    }

    @GetMapping("/token")
    @Operation(summary = "get kakao login callback", description = "카카오 로그인 콜백을 해서 사용자 로그인/회원가입을 진행합니다.")
    public ResponseEntity<ApiResponse<String>> kakaoCallBack(@RequestParam("code") String code) {
        String jwtToken = kakaoAuthService.kakaoCallBack(code);
        ApiResponse<String> apiResponse = new ApiResponse<>(HttpResult.OK, "카카오 로그인 성공",
            HttpStatus.OK, jwtToken);
        return ResponseEntity.ok(apiResponse);
    }
}
