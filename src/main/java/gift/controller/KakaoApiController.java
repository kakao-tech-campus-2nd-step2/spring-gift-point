package gift.controller;

import gift.service.KakaoApiService;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/kakao")
@Tag(name = "Kakao API", description = "카카오 인증 관련 API")
public class KakaoApiController {
    private final KakaoApiService kakaoApiService;
    private final UserService userService;

    public KakaoApiController(KakaoApiService kakaoApiService, UserService userService) {
        this.kakaoApiService = kakaoApiService;
        this.userService = userService;
    }

    @GetMapping("/login")
    @Operation(summary = "카카오 로그인 페이지로 리디렉션")
    public String kakaoLogin() {
        return "redirect:" + kakaoApiService.kakaoLogin();
    }

    @GetMapping
    @Operation(summary = "카카오 인가 코드 처리",
            description = "인가 코드를 받아 액세스 토큰을 생성하고 사용자 정보를 등록",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    public String kakaoLoginVerify(@RequestParam("code") String authorizationCode, Model model) {
        String accessToken = kakaoApiService.getAccessToken(authorizationCode);
        String email = kakaoApiService.getEmailFromToken(accessToken);
        userService.kakaoSign(email);
        model.addAttribute("accessToken", accessToken);

        return "kakaoLoginSuccess";
    }
}
