package gift.service;

import gift.common.enums.Role;
import gift.common.enums.SocialLoginType;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.security.JwtProvider;
import gift.service.dto.KakaoInfoDto;
import gift.service.dto.KakaoTokenDto;
import gift.service.dto.LoginDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final KakaoApiCaller kakaoApiCaller;
    private final KakaoTokenService kakaoTokenService;

    public OAuthService(MemberRepository memberRepository, JwtProvider jwtProvider, KakaoApiCaller kakaoApiCaller, KakaoTokenService kakaoTokenService) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.kakaoApiCaller = kakaoApiCaller;
        this.kakaoTokenService = kakaoTokenService;
    }

    @Transactional
    public LoginDto signIn(String code, String redirectUrl) {
        KakaoTokenDto kakaoTokenDto = kakaoApiCaller.getKakaoAccessToken(code, redirectUrl);
        KakaoInfoDto infoDto = kakaoApiCaller.getKakaoMemberInfo(kakaoTokenDto.accessToken());
        Member member = memberRepository.findByEmail(infoDto.email())
                .orElseGet(() -> memberRepository.save(new Member(infoDto.email(), "", infoDto.name(), Role.USER, SocialLoginType.KAKAO)));
        member.checkLoginType(SocialLoginType.KAKAO);

        kakaoTokenService.saveToken(member.getId(), kakaoTokenDto);
        String token = jwtProvider.generateToken(member.getId(), member.getEmail(), member.getRole());
        return LoginDto.of(token, member.getName());
    }

    public void signOut(Long memberId) {
        String accessToken = kakaoTokenService.refreshIfAccessTokenExpired(memberId);
        kakaoApiCaller.signOutKakao(accessToken);
        kakaoTokenService.deleteAccessToken(memberId);
        kakaoTokenService.deleteRefreshToken(memberId);
    }
}
