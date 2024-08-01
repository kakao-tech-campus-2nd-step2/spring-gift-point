package gift.api.member.service;

import gift.api.member.dto.TokenResponse;
import gift.api.member.dto.UserInfoResponse;
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

    public TokenResponse loginKakao(String code) {
        ResponseEntity<TokenResponse> tokenResponse = kakaoService.obtainToken(code);
        ResponseEntity<UserInfoResponse> userInfoResponse = kakaoService.obtainUserInfo(tokenResponse);
        memberService.verifyEmail(userInfoResponse.getBody().kakaoAccount());
        memberService.saveKakaoToken(userInfoResponse.getBody().kakaoAccount().email(),
            tokenResponse.getBody().accessToken());
        return tokenResponse.getBody();
    }
}
