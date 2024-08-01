package gift.application.member;

import gift.application.member.dto.MemberCommand;
import gift.application.member.dto.MemberModel;
import gift.application.member.dto.OAuthCommand;
import gift.application.member.service.MemberService;
import gift.application.member.service.MemberKakaoService;
import gift.application.token.TokenManager;
import gift.model.token.KakaoToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MemberFacade {

    private final MemberService memberService;
    private final MemberKakaoService memberKakaoService;
    private final TokenManager tokenManager;

    public MemberFacade(MemberService memberService, MemberKakaoService memberKakaoService,
        TokenManager tokenManager) {
        this.memberService = memberService;
        this.memberKakaoService = memberKakaoService;
        this.tokenManager = tokenManager;
    }

    public MemberModel.InfoAndJwt socialLogin(OAuthCommand.Login command, String redirectUrl) {
        KakaoToken token = tokenManager.getTokenByAuthorizationCode(
            command.authorizationCode(), redirectUrl);
        OAuthCommand.MemberInfo memberInfo = memberKakaoService.getMemberInfo(
            token.getAccessToken());
        MemberCommand.Create create = memberInfo.toCreateCommand();
        MemberModel.InfoAndJwt infoAndJwt = memberService.socialLogin(create);
        tokenManager.saveToken(infoAndJwt.info().id(), token);
        return infoAndJwt;
    }

}
