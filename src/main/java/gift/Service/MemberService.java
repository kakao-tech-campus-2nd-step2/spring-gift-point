package gift.Service;

import gift.DTO.MemberDTO;
import gift.Model.Member;
import gift.Repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    public Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email);
    }
    public void signupMember(MemberDTO memberDTO){
        Member member = new Member(memberDTO.getId(), memberDTO.getEmail(), memberDTO.getPassword(),memberDTO.getAccessToken());
        checkMember(member.getEmail());
        memberRepository.save(member);
    }
    public void checkMember(String email){
        Member checkMember = getMemberByEmail(email);
        if (checkMember == null){
            throw new IllegalArgumentException("중복");
        }
    }

}
