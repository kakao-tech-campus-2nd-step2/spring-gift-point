package gift.Controller;

import gift.DTO.KakaoUserDTO;
import gift.Service.KakaoAuthService;
import gift.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class KakaoAuthController {

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/auth/kakao/callback")
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
