package gift.Service;

import gift.DTO.MemberDTO;
import gift.DTO.PointDTO;
import gift.DTO.RemainingPointsDTO;
import gift.Exception.BadRequestException;
import gift.Exception.ForbiddenException;
import gift.Exception.UnauthorizedException;
import gift.Model.Member;
import gift.Repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    public List<Member> getAllMembers(){
        return memberRepository.findAll();
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
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public void checkMember(MemberDTO memberDTO){
        Member checkMember = getMemberByEmail(memberDTO.getEmail());
        if(!checkMember.getEmail().equals(memberDTO.getEmail()) || !checkMember.getPassword().equals(memberDTO.getPassword())){
            throw new ForbiddenException("403 Forbidden : Invalid email or password");
        }
    }
    public void checkMemberByEmail(String email){
        Member checkMember = memberRepository.findByEmail(email);
        if (checkMember == null){
            throw new UnauthorizedException("401 Unauthorized : Invalid or missing token");
        }
    }

    public PointDTO getPoint(String email){
        Member member = memberRepository.findByEmail(email);
        return new PointDTO(member.getPoint());
    }

    public RemainingPointsDTO usePoint(String email, int point){
        Member member = memberRepository.findByEmail(email);
        if(!member.checkPoint(point)){ // 가지고 있는 point 1000 미만 or 가지고있는 포인트가 사용할 포인트 보다 더 작을 때
            throw new BadRequestException("400 Bad Request : Invalid input");
        }
        int remainingPoint = member.usePoint(point);
        memberRepository.save(member);
        return new RemainingPointsDTO(remainingPoint);
    }

    public void updateMember(Member member){
        memberRepository.save(member);
    }
}
