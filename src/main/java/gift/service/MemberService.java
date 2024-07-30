package gift.service;

import static gift.util.JwtUtil.generateJwtToken;
import gift.dto.MemberDto;
import gift.exception.ForbiddenException;
import gift.domain.Member;
import gift.repository.MemberRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public String registerMember(MemberDto memberDto) {
        if (memberRepository.findByEmail(memberDto.email()) != null) {
            throw new RuntimeException("Email already registered");
        }

        Member newMember = new Member(memberDto.email(), memberDto.password(), memberDto.accessToken());
        memberRepository.save(newMember);
        return newMember.getEmail();
    }

    @Transactional(readOnly = true)
    public String login(MemberDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.email());
        if (member == null || !memberDto.password().equals(member.getPassword())) {
            throw new ForbiddenException("사용자 없거나 비밀번호 틀림");
        }

        return generateJwtToken(member);
    }

    @Transactional(readOnly = true)
    public Member findById(long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 멤버 없음: " + id));
        return member;
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(memberRepository.findByEmail(email));
    }
}
