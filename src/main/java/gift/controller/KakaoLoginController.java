package gift.controller;

import gift.service.KakaoLoginService;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "카카오 로그인", description = "카카오 로그인 관련 API")
@RestController
@RequestMapping("/api/kakao")
public class KakaoLoginController {
    private final KakaoLoginService kakaoLoginService;
    private final MemberService memberService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService, MemberService memberService) {
        this.kakaoLoginService = kakaoLoginService;
        this.memberService = memberService;
    }

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_url}")
    private String redirectUrl;

    @GetMapping("/login")
    public ResponseEntity<Void> kakaoLogin() {
        String uri = "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId
                + "&redirect_uri=" + redirectUrl
                + "&response_type=code";
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(uri));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/getauth")
    public ResponseEntity<?> getAuth(@RequestParam("code") String code) {
        String token = kakaoLoginService.getToken(code);
        String email = kakaoLoginService.getEmail(token);
        memberService.kakaoLogin(email, token);
        return ResponseEntity.ok().build();
    }
}
