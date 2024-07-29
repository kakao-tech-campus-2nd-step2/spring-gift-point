package gift.service;

import gift.common.util.JwtUtil;
import gift.dto.MemberRequest;
import gift.entity.Member;
import gift.repository.MemberRepository;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
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
            throw new DuplicateKeyException("이미 존재하는 이메일 : " + memberRequest.getEmail());
        }

        Member member = new Member(memberRequest.getEmail(), memberRequest.getPassword());
        memberRepository.save(member);

        return jwtUtil.createToken(member.getEmail());
    }

    public String login(String email, String password) {
        return memberRepository.findByEmail(email)
            .filter(member -> member.getPassword().equals(password))
            .map(member -> jwtUtil.createToken(member.getEmail()))
            .orElse(null);
    }

    public Member getMemberFromToken(String token) {
        try {
            String email = jwtUtil.extractEmail(token);
            Optional<Member> member = memberRepository.findByEmail(email);
            return member.orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token이 잘못되었습니다.");
        }
    }

}
