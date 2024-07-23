package gift.service;

import static gift.util.JwtUtil.generateJwtToken;
import gift.dto.MemberDto;
import gift.exception.ForbiddenException;
import gift.domain.Member;
import gift.repository.MemberRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String registerMember(MemberDto memberDto) {
        if (memberRepository.findByEmail(memberDto.getEmail()) != null) {
            throw new RuntimeException("Email already registered");
        }

        Member newMember = new Member(memberDto.getEmail(), memberDto.getPassword());
        memberRepository.save(newMember);
        return newMember.getEmail();
    }

    public String login(MemberDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.getEmail());
        if (member == null || !memberDto.getPassword().equals(member.getPassword())) {
            throw new ForbiddenException("사용자 없거나 비밀번호 틀림");
        }

        return generateJwtToken(member);
    }

    public Member findById(long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 멤버 없음: " + id));
        return member;
    }
}
