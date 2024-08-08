package gift.controller;

import gift.domain.member.TokenResponse;
import gift.domain.member.UrlResponse;
import gift.service.KakaoLoginService;
import gift.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth/kakao")
public class KakaoLoginController {

    private final MemberService memberService;
    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(MemberService memberService, KakaoLoginService kakaoLoginService) {
        this.memberService = memberService;
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/url")
    public UrlResponse loginUrl() {
        return new UrlResponse(kakaoLoginService.getLoginUrl());
    }

    @GetMapping("/code")
    public TokenResponse login(@RequestParam String code) {
        String kakaoToken = kakaoLoginService.getToken(code);
        Long kakaoId = kakaoLoginService.getKakaoId(kakaoToken);
        return new TokenResponse(memberService.kakaoLogin(kakaoId, kakaoToken));
    }
}
