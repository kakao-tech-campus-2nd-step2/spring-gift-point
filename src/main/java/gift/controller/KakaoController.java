package gift.controller;

import gift.domain.Member;
import gift.dto.TokenResponse;
import gift.service.KakaoService;
import gift.service.KakaoTokenService;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@Tag(name = "Kakao API", description = "카카오 API 관련 엔드포인트")
public class KakaoController {

    private final MemberService memberService;
    private final KakaoService kakaoLoginService;
    private final KakaoTokenService kakaoTokenService;

    public KakaoController(MemberService memberService, KakaoService kakaoLoginService, KakaoTokenService kakaoTokenService) {
        this.memberService = memberService;
        this.kakaoLoginService = kakaoLoginService;
        this.kakaoTokenService = kakaoTokenService;
    }

    @PostMapping("/kakao/login")
    @Operation(summary = "카카오 로그인", description = "카카오 인증 코드를 사용하여 로그인합니다.")
    public ResponseEntity<Object> kakaoLogin(@RequestParam String code) {
        try {
            String accessToken = kakaoLoginService.getAccessToken(code);
            Map<String, Object> userInfo = kakaoLoginService.getUserInfo(accessToken);

            String email = "kakao_" + userInfo.get("id") + "@kakao.com";;

            Member member = memberService.findByEmail(email);

            if (member == null) {
                String password = memberService.generateTemporaryPassword();
                member = new Member(null, email, password);
                memberService.register(member);
            }

            kakaoTokenService.saveToken(member.getId(), email, accessToken);

            String token = memberService.generateToken(member);

            return ResponseEntity.ok(new TokenResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카카오 로그인 실패: " + e.getMessage());
        }
    }

    @GetMapping("/")
    @Operation(summary = "카카오 콜백 처리", description = "카카오 인증 후 리다이렉트되는 콜백을 처리합니다.")
    public ResponseEntity<Object> handleKakaoCallback(@RequestParam String code) {
        RestTemplate restTemplate = new RestTemplate();
        String postUrl = "http://localhost:8080/kakao/login?code=" + code;
        ResponseEntity<String> response = restTemplate.postForEntity(postUrl, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("로그인 성공!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 실패");
        }
    }

}
