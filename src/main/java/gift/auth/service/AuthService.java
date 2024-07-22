package gift.auth.service;

import gift.auth.domain.AuthInfo;
import gift.auth.dto.LoginRequestDto;
import gift.global.security.TokenManager;
import gift.member.domain.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenManager tokenManager;
    public static final String BEARER_TYPE = "Bearer";

    public AuthService(MemberRepository memberRepository, TokenManager tokenManager) {
        this.memberRepository = memberRepository;
        this.tokenManager = tokenManager;
    }

    public Map<String, String> login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmailAndPassword(loginRequestDto.email(), loginRequestDto.password())
                .orElseThrow(MemberNotFoundException::new);
        String accessToken = BEARER_TYPE + " " + tokenManager.createAccessToken(new AuthInfo(member));
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", accessToken);
        return headers;
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }
}
