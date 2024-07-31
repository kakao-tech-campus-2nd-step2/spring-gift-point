package gift.member.oauth;

import gift.api.kakaoAuth.KakaoAuthClient;
import gift.api.kakaoAuth.KakaoTokenResponse;
import gift.common.utils.TokenProvider;
import gift.member.MemberRepository;
import gift.member.model.Member;
import gift.member.model.MemberResponse;
import gift.member.oauth.model.OauthToken;
import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OauthService {

    private final KakaoAuthClient kakaoAuthClient;

    private final TokenProvider tokenProvider;

    private final MemberRepository memberRepository;

    private final OauthTokenRepository oauthTokenRepository;

    public OauthService(KakaoAuthClient kakaoAuthClient, TokenProvider tokenProvider,
        MemberRepository memberRepository,
        OauthTokenRepository oauthTokenRepository) {
        this.kakaoAuthClient = kakaoAuthClient;
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
        this.oauthTokenRepository = oauthTokenRepository;
    }

    public URI getKakaoAuthorization() {
        return kakaoAuthClient.getAuthorization();
    }

    @Transactional
    public MemberResponse loginByKakao(String authorizationCode) {
        KakaoTokenResponse kakaoTokenReponse = kakaoAuthClient.getKakaoTokenResponse(
            authorizationCode);
        String email = kakaoAuthClient.getEmail(kakaoTokenReponse.accessToken());
        Member member = saveToken(email, kakaoTokenReponse);
        return new MemberResponse("Bearer", tokenProvider.generateToken(member));
    }

    private Member saveToken(String email, KakaoTokenResponse kakaoTokenReponse) {
        if (oauthTokenRepository.existsByProviderAndEmail("kakao", email)) {
            OauthToken oauthToken = oauthTokenRepository.findByProviderAndEmail("kakao",
                email);
            oauthToken.updateToken(kakaoTokenReponse.accessToken(),
                kakaoTokenReponse.refreshToken());
            return oauthToken.getMember();
        }

        Member member = new Member(email, "", "nickname", "Member");
        memberRepository.save(member);
        OauthToken oauthToken = new OauthToken("kakao", email,
            kakaoTokenReponse.accessToken(), kakaoTokenReponse.accessTokenExpiresIn(), kakaoTokenReponse.refreshToken(), member);
        oauthTokenRepository.save(oauthToken);
        return member;
    }

    @Transactional
    public void verifyToken(String accessToken) {
        OauthToken oauthToken = oauthTokenRepository.findByAccessToken(accessToken)
            .orElseThrow();
        if(oauthToken.isNotValid()) {
            KakaoTokenResponse kakaoTokenResponse = kakaoAuthClient.refreshAccessToken(
                oauthToken.getRefreshToken());
            oauthToken.updateToken(kakaoTokenResponse.accessToken(),
                kakaoTokenResponse.refreshToken());
        }
    }
}
