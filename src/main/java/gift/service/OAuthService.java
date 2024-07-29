package gift.service;

import gift.component.KakaoApiComponent;
import gift.dto.LoginResponse;
import gift.dto.OAuthLoginRequest;
import gift.exception.user.MemberNotFoundException;
import gift.jwt.JwtUtil;
import gift.model.Member;
import gift.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {
    private final MemberRepository memberRepository;
    private final KakaoApiComponent kakaoApiComponent;

    public OAuthService(MemberRepository memberRepository, KakaoApiComponent kakaoApiComponent) {
        this.memberRepository = memberRepository;
        this.kakaoApiComponent = kakaoApiComponent;
    }

    public String getAccessToken(String code) {
        return kakaoApiComponent.getAccessToken(code);
    }

    public String getMemberProfileId(String accessToken) {
        return kakaoApiComponent.getMemberProfileId(accessToken);
    }

    public Member register(OAuthLoginRequest request) {
        Member member = new Member(
                request.id(),
                "password",
                request.accessToken()
        );
        return memberRepository.save(member);
    }

    public LoginResponse login(OAuthLoginRequest request) {
        Member member = memberRepository.findByEmail(request.id())
                .orElseThrow(() -> new MemberNotFoundException("해당 유저가 존재하지 않습니다."));
        LoginResponse response = new LoginResponse(JwtUtil.createToken(member.getEmail()));
        return response;
    }

    @Transactional
    public void updateAccessToken(OAuthLoginRequest request) {
        Member member = memberRepository.findByEmail(request.id())
                .orElseThrow(() -> new MemberNotFoundException("해당 유저가 존재하지 않습니다."));
        member.updateAccessToken(request.accessToken());
    }

    public boolean isMemberAlreadyRegistered(OAuthLoginRequest request) {
        return memberRepository.findByEmail(request.id()).isPresent();
    }
}
