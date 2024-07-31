package gift.auth.service.facade;

import gift.auth.service.KakaoOAuthService;
import gift.common.annotation.Facade;
import gift.member.service.MemberService;
import gift.member.service.command.MemberInfoCommand;
import gift.member.service.dto.MemberSignInInfo;
import org.apache.commons.lang3.RandomStringUtils;

@Facade
public class OAuthFacade {
    private final KakaoOAuthService kakaoOAuthService;
    private final MemberService memberService;

    public OAuthFacade(KakaoOAuthService kakaoOAuthService, MemberService memberService) {
        this.kakaoOAuthService = kakaoOAuthService;
        this.memberService = memberService;
    }

    public MemberSignInInfo kakaoCallBack(final String code) {
        var username = kakaoOAuthService.callBack(code);

        MemberInfoCommand memberInfo = new MemberInfoCommand(username.email(),
                RandomStringUtils.randomAlphanumeric(10), true);

        if (memberService.checkUser(username.email())) {
            var token = memberService.signIn(memberInfo).token();
            return MemberSignInInfo.of(token);
        }
        
        var token = memberService.signUp(memberInfo).token();
        return MemberSignInInfo.of(token);
    }
}
