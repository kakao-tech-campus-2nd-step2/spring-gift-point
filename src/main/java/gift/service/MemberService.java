package gift.service;

import gift.config.JwtConfig;
import gift.domain.Member;
import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.exception.ErrorMessage;
import gift.repository.MemberRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtConfig jwtConfig;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtConfig jwtConfig) {
        this.memberRepository = memberRepository;
        this.jwtConfig = jwtConfig;
    }

    public MemberResponse registerMember(MemberRequest requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException(ErrorMessage.MEMBER_EMAIL_ALREADY_EXISTS);
        }

        Member member = new Member(requestDto.getEmail(), requestDto.getPassword());
        memberRepository.save(member);

        String token = jwtConfig.generateToken(requestDto.getEmail());
        return new MemberResponse(token);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException(ErrorMessage.EMAIL_NOT_FOUND));
        if (member != null && member.getPassword().equals(loginRequest.getPassword())) {
            String token = jwtConfig.generateToken(loginRequest.getEmail());
            return new LoginResponse(token);
        } else {
            throw new IllegalArgumentException(ErrorMessage.INVALID_LOGIN_CREDENTIALS);
        }
    }

    public Member getMember(String token) {
        return memberRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("해당 토큰을 가진 회원이 없습니다."));
    }

    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow();
    }
}
