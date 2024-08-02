package gift.api.member.service;

import gift.api.member.dto.KakaoAccount;
import gift.api.member.dto.KakaoLoginResponse;
import gift.api.member.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberFacade {

    private final MemberService memberService;
    private final KakaoService kakaoService;

    public MemberFacade(MemberService memberService, KakaoService kakaoService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
    }

    public KakaoLoginResponse loginKakao(String code) {
        ResponseEntity<TokenResponse> tokenResponse = kakaoService.obtainToken(code);
        String kakaoAccessToken = tokenResponse.getBody()
            .accessToken();
        KakaoAccount kakaoAccount = kakaoService.obtainUserInfo(tokenResponse)
            .getBody()
            .kakaoAccount();
        memberService.verifyEmail(kakaoAccount);
        memberService.saveKakaoToken(kakaoAccount.email(), kakaoAccessToken);
        return KakaoLoginResponse.of(
            memberService.issueAccessToken(kakaoAccount.email()), kakaoAccessToken);
    }
}
