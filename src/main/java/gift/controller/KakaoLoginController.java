package gift.controller;

import gift.auth.DTO.TokenDTO;
import gift.service.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/login/kakao")
@Tag(name = "카카오톡 로그인과 관련된 api", description = "/api/login/kakao 경로로 요청할 경우 자동적으로 토큰이 반환 됩니다.")
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService KakaoLoginService;

    @GetMapping
    @Operation(summary = "카카오 인증 URL 요청", description = "카카오 인증 URL로 리다이렉트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "카카오 인증 URL로 리다이렉트 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인해 리다이렉트 실패")
    })
    public void getCode(HttpServletResponse response) throws IOException {
        String kakaoAuthUrl = KakaoLoginService.getAuthorizeUrl();
        response.sendRedirect(kakaoAuthUrl);
    }

    @GetMapping("/token")
    @Operation(summary = "카카오 인증 코드로 토큰 요청", description = "카카오 인증 코드를 사용하여 액세스 토큰을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 인증 코드로 인해 토큰 생성 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인해 토큰 생성 실패")
    })
    public TokenDTO getTokenGET(@RequestParam("code") String code) {
        var kakaoToken = KakaoLoginService.getToken(code);
        return KakaoLoginService.createToken(kakaoToken);
    }
}
