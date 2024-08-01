package gift.Service;

import gift.DTO.MemberDTO;
import gift.Exception.BadRequestException;
import gift.Exception.ForbiddenException;
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
        if(getMemberByEmail(memberDTO.getEmail()) != null){
            throw new BadRequestException("400 Bad Request : Invalid input");
        }
        memberRepository.save(member);
    }
    public void checkMember(MemberDTO memberDTO){
        Member checkMember = getMemberByEmail(memberDTO.getEmail());
        if(!checkMember.getEmail().equals(memberDTO.getEmail()) || !checkMember.getPassword().equals(memberDTO.getPassword())){
            throw new ForbiddenException("403 Forbidden : Invalid email or password");
        }
    }

}
