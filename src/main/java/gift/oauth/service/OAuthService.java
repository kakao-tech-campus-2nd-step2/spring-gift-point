package gift.oauth.service;

import gift.domain.member.entity.Member;
import gift.domain.member.repository.MemberRepository;
import gift.kakaoApi.dto.userInfo.KakaoAccount;
import gift.kakaoApi.service.KakaoApiService;
import gift.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OAuthService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final KakaoApiService kakaoApiService;

    public OAuthService(MemberRepository memberRepository,
        JwtUtil jwtUtil,
        KakaoApiService kakaoApiService) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.kakaoApiService = kakaoApiService;
    }

    public String getAccessToken(String code) {
        String kakaoAccessToken = kakaoApiService.getKakaoToken(code).accessToken();
        return jwtUtil.generateToken(registerOrLoginKakoMember(kakaoAccessToken));

    }

    public Member registerOrLoginKakoMember(String kakaoAccessToken) {

        KakaoAccount kakaoAccount = kakaoApiService.getKakaoAccount(kakaoAccessToken)
            .kakaoAccount();
        Member member = memberRepository.findByEmail(kakaoAccount.email())
            .orElseGet(() -> new Member(kakaoAccount.email(), ""));

        member.updateKakaoAccessToken(kakaoAccessToken);

        return memberRepository.save(member);
    }
}

