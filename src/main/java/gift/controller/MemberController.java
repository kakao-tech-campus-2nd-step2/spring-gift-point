package gift.controller;

import gift.service.MemberService;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-url}")
    private String kakaoRedirectUri;

    @Autowired
    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/kakao")
    public ResponseEntity<Void> redirectToKakaoAuth() {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                + kakaoClientId + "&redirect_uri=" + kakaoRedirectUri;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", kakaoAuthUrl);
        return ResponseEntity.status(302).headers(headers).build();
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> handleKakaoCallback(@RequestParam("code") String code) {
        try {
            String token = memberService.kakaoLogin(code);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            headers.add("Content-Type", "application/json");
            return ResponseEntity.ok().headers(headers).body("{\"token\": \"" + token + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Authentication failed");
        }
    }
}
