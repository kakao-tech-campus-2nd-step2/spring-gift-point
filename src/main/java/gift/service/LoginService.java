package gift.service;

import gift.authentication.token.JwtProvider;
import gift.authentication.token.KakaoToken;
import gift.authentication.token.Token;
import gift.config.KakaoProperties;
import gift.domain.Member;
import gift.web.client.KakaoClient;
import gift.web.client.dto.KakaoAccount;
import gift.web.client.dto.KakaoInfo;
import gift.web.dto.response.LoginResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final KakaoClient kakaoClient;

    private final KakaoProperties kakaoProperties;

    private final MemberService memberService;

    private final JwtProvider jwtProvider;

    public LoginService(KakaoClient kakaoClient, KakaoProperties kakaoProperties,
        MemberService memberService, JwtProvider jwtProvider) {
        this.kakaoClient = kakaoClient;
        this.kakaoProperties = kakaoProperties;
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public LoginResponse kakaoLogin(final String authorizationCode){
        final KakaoToken kakaoToken = getToken(authorizationCode);
        final KakaoInfo kakaoInfo = getInfo(kakaoToken);

        final KakaoAccount kakaoAccount = kakaoInfo.getKakaoAccount();
        final Member member = memberService.findOrCreateMember(kakaoAccount);

        final Token accessToken = jwtProvider.generateToken(member, kakaoToken.getAccessToken());

        return new LoginResponse(accessToken.getValue());
    }

    private KakaoToken getToken(final String authorizationCode) {
        return kakaoClient.getToken(
            kakaoProperties.getTokenUrlAsUri(),
            authorizationCode,
            kakaoProperties.getClientId(),
            kakaoProperties.getRedirectUri(),
            kakaoProperties.getGrantType());
    }

    private KakaoInfo getInfo(final KakaoToken kakaoToken) {
        return kakaoClient.getKakaoInfo(
            kakaoProperties.getUserInfoUrlAsUri(),
            getBearerToken(kakaoToken));
    }

    private String getBearerToken(final KakaoToken kakaoToken) {
        return kakaoToken.getTokenType() + " " + kakaoToken.getAccessToken();
    }
}
