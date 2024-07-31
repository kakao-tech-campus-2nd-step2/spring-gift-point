package gift.domain.member.service;

import gift.auth.jwt.JwtProvider;
import gift.auth.jwt.JwtToken;
import gift.domain.member.dto.MemberLoginRequest;
import gift.domain.member.dto.MemberRequest;
import gift.domain.member.entity.AuthProvider;
import gift.domain.member.entity.Role;
import gift.domain.member.entity.Member;
import gift.domain.member.repository.MemberJpaRepository;
import gift.exception.InvalidUserInfoException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberJpaRepository memberJpaRepository, JwtProvider jwtProvider) {
        this.memberJpaRepository = memberJpaRepository;
        this.jwtProvider = jwtProvider;
    }

    public JwtToken signUp(MemberRequest memberRequest) {
        Member member = memberRequest.toMember();
        Member savedMember = memberJpaRepository.save(member);
        
        return jwtProvider.generateToken(savedMember);
    }

    public JwtToken login(MemberLoginRequest memberLoginRequest) {
        Member member = memberJpaRepository.findByEmail(memberLoginRequest.email())
            .orElseThrow(() -> new InvalidUserInfoException("error.invalid.userinfo.email"));

        if (member.getAuthProvider() != AuthProvider.LOCAL) {
            throw new InvalidUserInfoException("error.invalid.userinfo.provider");
        }

        if (!member.checkPassword(memberLoginRequest.password())) {
            throw new InvalidUserInfoException("error.invalid.userinfo.password");
        }

        return jwtProvider.generateToken(member);
    }

    public Role verifyRole(JwtToken jwtToken) {
        return jwtProvider.getAuthorization(jwtToken.token());
    }
}
