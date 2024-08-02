package gift.Controller;

import gift.DTO.KakaoUserDTO;
import gift.Service.KakaoAuthService;
import gift.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "소셜 로그인 관련 서비스", description = " ")
public class KakaoAuthController {

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "카카오 인가 코드로 카카오 토큰을 받아온다.", description = "카카오 인가코드를 기반으로 토큰을 조회해서 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated with Kakao and JWT tokens issued"),
            @ApiResponse(responseCode = "400", description = "Invalid Kakao authorization code"),
            @ApiResponse(responseCode = "500", description = "Internal server error during Kakao authentication")
    })
    @GetMapping("/api/social/token/kakao")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam String code) {
        KakaoUserDTO kakaoUserDTO = kakaoAuthService.handleKakaoLogin(code);

        // JWT 토큰 생성(사용자에게 주는 토큰)
        String accessToken = jwtTokenProvider.createAccessToken(kakaoUserDTO.getKakaoId());
        String refreshToken = jwtTokenProvider.createRefreshToken(kakaoUserDTO.getKakaoId());

        // 사용자 정보와 JWT 토큰을 반환
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        response.put("kakaoId", kakaoUserDTO.getKakaoId());
        response.put("email", kakaoUserDTO.getEmail());

        return ResponseEntity.ok(response);
    }
}
