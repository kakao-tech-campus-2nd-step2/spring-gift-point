package gift.service;

import gift.model.CreateJwtToken;
import gift.dto.LogInMemberDTO;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MemberService {
    private final CreateJwtToken createJwtToken;
    private final MemberRepository memberRepository;

    public MemberService(CreateJwtToken createJwtToken, MemberRepository memberRepository) {
        this.createJwtToken = createJwtToken;
        this.memberRepository = memberRepository;
    }

    public String signin(LogInMemberDTO memberDTO){
        Optional<Member> Optionalmember = memberRepository.findByEmail(memberDTO.getEmail());
        Optionalmember.ifPresent(member -> {
            throw new IllegalArgumentException("이미 가입한 이메일 입니다.");
        });
        Member member = new Member(memberDTO.getEmail(), memberDTO.getPassword());
        memberRepository.save(member);
        return createJwtToken.createJwt(member.getId(), member.getEmail());
    }

    public String login(LogInMemberDTO member) {
        //1. 이메일 확인
        Member loginMember = comfirmEmail(member.getEmail());
        //2. 패스워드 확인
        if(loginMember.comfirmPassword(member.getPassword())){
            //3. 토큰 발급
            return createJwtToken.createJwt(loginMember.getId(), loginMember.getEmail());
        }
        throw new IllegalStateException("로그인에 실패했습니다.");
    }

    private Member comfirmEmail(String email){
        if(!memberRepository.existsByEmail(email)){
            throw new IllegalStateException("이메일을 확인해주세요.");
        }
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("해당 멤버가 없습니다."));
    }
}
