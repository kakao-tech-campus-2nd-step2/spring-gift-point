package gift.domain.member.service;

import gift.auth.jwt.JwtProvider;
import gift.auth.jwt.JwtToken;
import gift.domain.member.entity.AuthProvider;
import gift.domain.member.entity.Member;
import gift.domain.member.entity.OauthToken;
import gift.domain.member.entity.Role;
import gift.domain.member.repository.OauthTokenJpaRepository;
import gift.domain.member.repository.MemberJpaRepository;
import gift.exception.InvalidUserInfoException;
import gift.external.api.kakao.dto.KakaoToken;
import gift.external.api.kakao.dto.KakaoUserInfo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class KakaoLoginService {

    private final OauthApiProvider<KakaoToken, KakaoUserInfo> kakaoApiProvider; // 좀 이상한 것 같다.. 생각해보기
    private final MemberJpaRepository memberJpaRepository;
    private final OauthTokenJpaRepository oauthTokenJpaRepository;
    private final JwtProvider jwtProvider;

    public KakaoLoginService(
        OauthApiProvider<KakaoToken, KakaoUserInfo> kakaoApiProvider,
        MemberJpaRepository memberJpaRepository,
        OauthTokenJpaRepository oauthTokenJpaRepository,
        JwtProvider jwtProvider
    ) {
        this.kakaoApiProvider = kakaoApiProvider;
        this.memberJpaRepository = memberJpaRepository;
        this.oauthTokenJpaRepository = oauthTokenJpaRepository;
        this.jwtProvider = jwtProvider;
    }

    public String getAuthCodeUrl() {
        return kakaoApiProvider.getAuthCodeUrl();
    }

    @Transactional
    public JwtToken login(String code) {
        KakaoToken kakaoToken = kakaoApiProvider.getToken(code);
        String accessToken = kakaoToken.accessToken();
        String refreshToken = kakaoToken.refreshToken();

        kakaoApiProvider.validateAccessToken(accessToken);
        KakaoUserInfo userInfo = kakaoApiProvider.getUserInfo(accessToken);

        String email = userInfo.kakaoAccount().email();
        String name = userInfo.kakaoAccount().profile().nickname();

        return memberJpaRepository.findByEmail(email)
            .map(member -> {
                if (member.getAuthProvider() != AuthProvider.KAKAO) {
                    throw new InvalidUserInfoException("error.invalid.userinfo.provider");
                }
                oauthTokenJpaRepository.save(new OauthToken(null, member, AuthProvider.KAKAO, accessToken, refreshToken));
                return jwtProvider.generateToken(member);
            })
            .orElseGet(() -> signUp(name, email, accessToken, refreshToken));
    }

    private JwtToken signUp(String name, String email, String accessToken, String refreshToken) {
        Member member = new Member(null, name, email, "kakao", Role.USER, AuthProvider.KAKAO);
        Member savedMember = memberJpaRepository.save(member);

        oauthTokenJpaRepository.save(new OauthToken(null, savedMember, AuthProvider.KAKAO, accessToken, refreshToken));

        return jwtProvider.generateToken(savedMember);
    }
}
