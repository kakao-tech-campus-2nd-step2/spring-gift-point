package gift.service;

import gift.config.JwtConfig;
import gift.domain.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtConfig jwtConfig;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtConfig jwtConfig) {
        this.memberRepository = memberRepository;
        this.jwtConfig = jwtConfig;
        this.jwtUtil = new JwtUtil(jwtConfig);
    }

    public void register(Member member) {
        memberRepository.save(member);
    }

    public String login(String email, String password) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent() && member.get().getPassword().equals(password)) {
            return jwtUtil.generateToken(member.get());
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }
    public Member save(Member member) {
        return memberRepository.save(member);
    }
}
