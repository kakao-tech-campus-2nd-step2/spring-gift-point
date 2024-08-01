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
        if(checkMember(member.getEmail())){
            throw new IllegalArgumentException("중복");
        }
        memberRepository.save(member);
    }
    public boolean checkMember(String email){
        Member checkMember = getMemberByEmail(email);
        return checkMember != null;
    }

}
