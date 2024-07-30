package gift.service;

import gift.common.enums.Role;
import gift.common.enums.SocialLoginType;
import gift.common.properties.KakaoProperties;
import gift.controller.dto.response.TokenResponse;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.security.JwtProvider;
import gift.service.dto.KakaoTokenDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final KakaoProperties properties;
    private final KakaoApiCaller kakaoApiCaller;
    private final KakaoTokenService kakaoTokenService;

    public OAuthService(MemberRepository memberRepository, JwtProvider jwtProvider, KakaoProperties kakaoProperties, KakaoApiCaller kakaoApiCaller, KakaoTokenService kakaoTokenService) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.properties = kakaoProperties;
        this.kakaoApiCaller = kakaoApiCaller;
        this.kakaoTokenService = kakaoTokenService;
    }

    @Transactional
    public TokenResponse signIn(String code) {
        return signIn(code, properties.redirectUrl());
    }

    @Transactional
    public TokenResponse signIn(String code, String redirectUrl) {
        KakaoTokenDto kakaoTokenDto = kakaoApiCaller.getKakaoAccessToken(code, redirectUrl);
        String email = kakaoApiCaller.getKakaoMemberInfo(kakaoTokenDto.accessToken());

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email, "", Role.USER, SocialLoginType.KAKAO)));
        member.checkLoginType(SocialLoginType.KAKAO);

        kakaoTokenService.saveToken(member.getId(), kakaoTokenDto);
        String token = jwtProvider.generateToken(member.getId(), member.getEmail(), member.getRole());
        return TokenResponse.from(token);
    }

    public void signOut(Long memberId) {
        String accessToken = kakaoTokenService.refreshIfAccessTokenExpired(memberId);
        kakaoApiCaller.signOutKakao(accessToken);
        kakaoTokenService.deleteAccessToken(memberId);
        kakaoTokenService.deleteRefreshToken(memberId);
    }
}
