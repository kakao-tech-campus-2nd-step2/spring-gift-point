package gift.auth.controller;

import gift.auth.service.KakaoLoginService;
import gift.auth.dto.LoginMemberToken;
import gift.member.service.MemberService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/member/kakao")
@Controller
public class KakaoController {

    private final KakaoLoginService kakaoLoginService;
    private final MemberService memberService;

    public KakaoController(KakaoLoginService kakaoLoginService, MemberService memberService) {
        this.kakaoLoginService = kakaoLoginService;
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String kakaoLoginTry() {
        return "redirect:" + kakaoLoginService.getConnectionUrl();
    }

    @GetMapping("/login/callback")
    public ResponseEntity<LoginMemberToken> kakaoLogin(@RequestParam @NotNull String code) {

        String token = kakaoLoginService.getToken(code);

        return ResponseEntity.ok(kakaoLoginService.getKakaoUser(token));

    }
}
