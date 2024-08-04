package gift.service;

import gift.common.enums.SocialLoginType;
import gift.common.exception.AuthenticationException;
import gift.controller.dto.request.SignInRequest;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.security.JwtProvider;
import gift.service.dto.LoginDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public LoginDto signIn(SignInRequest request) {
        Member member = findEmailAndPassword(request);
        member.checkLoginType(SocialLoginType.DEFAULT);
        String token = jwtProvider.generateToken(member.getId(), member.getEmail(), member.getRole());
        return LoginDto.of(token, member.getName(), member.getRole());
    }

    private Member findEmailAndPassword(SignInRequest request) {
        return memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new AuthenticationException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }
}
