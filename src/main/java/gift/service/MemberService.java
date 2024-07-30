package gift.service;

import gift.common.exception.conflict.EmailAlreadyExistsException;
import gift.common.exception.forbidden.InvalidCredentialsException;
import gift.common.exception.unauthorized.TokenErrorException;
import gift.common.exception.unauthorized.TokenNotFoundException;
import gift.common.util.JwtUtil;
import gift.dto.MemberRequest;
import gift.entity.Member;
import gift.repository.MemberRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public String register(MemberRequest memberRequest) {
        Optional<Member> existingMember = memberRepository.findByEmail(memberRequest.getEmail());
        if (existingMember.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        Member member = new Member(memberRequest.getEmail(), memberRequest.getPassword());
        memberRepository.save(member);

        return jwtUtil.createToken(member.getEmail());
    }

    public String login(String email, String password) {
        return memberRepository.findByEmail(email)
            .filter(member -> member.getPassword().equals(password))
            .map(member -> jwtUtil.createToken(member.getEmail()))
            .orElseThrow(InvalidCredentialsException::new);
    }

    public Member getMemberFromToken(String token) {
        try {
            String email = jwtUtil.extractEmail(token);
            return memberRepository.findByEmail(email)
                .orElseThrow(TokenNotFoundException::new);
        } catch (Exception e) {
            throw new TokenErrorException();
        }
    }

}
