package gift.controller;

import gift.common.exception.unauthorized.TokenNotFoundException;
import gift.common.util.JwtUtil;
import gift.dto.KakaoAccessToken;
import gift.dto.KakaoUserInfo;
import gift.entity.Member;
import gift.service.KakaoService;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao")
@Tag(name = "Kakao API", description = "카카오 로그인 관련 API")
public class KakaoApiController {

    private final KakaoService kakaoService;

    public KakaoApiController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login")
    @Operation(summary = "카카오 로그인 URL", description = "카카오 로그인 URL을 반환합니다.")
    public ResponseEntity<String> getKakaoLoginUrl() {
        String loginUrl = kakaoService.getKakaoLogin();
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", loginUrl).build();
    }

    @GetMapping("/callback")
    @Operation(summary = "카카오 콜백", description = "카카오 로그인 후 리다이렉트되는 콜백 URL입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<String> kakaoCallback(
        @RequestParam(required = false) String code) {
        if (code == null) {
            throw new TokenNotFoundException();
        }

        String jwtToken = kakaoService.handleKakaoCallback(code);
        return ResponseEntity.ok("Bearer " + jwtToken);
    }

    @GetMapping("/user")
    @Operation(summary = "사용자 정보", description = "카카오 로그인 후 사용자 정보를 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "사용자가 존재하지 않음")
    })
    public ResponseEntity<KakaoUserInfo> getUserInfo(
        @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = authHeader.replace("Bearer ", "");
        KakaoUserInfo userInfo = kakaoService.getUserInfo(accessToken);

        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(userInfo);
    }

}
