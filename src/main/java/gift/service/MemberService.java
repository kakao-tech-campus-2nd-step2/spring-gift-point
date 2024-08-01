package gift.service;

import gift.dto.MemberDTO;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member saveMember(MemberDTO memberDTO) {
        Member member = new Member(null, memberDTO.name(), memberDTO.email(), memberDTO.password(),
            "user");
        return memberRepository.save(member);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

}