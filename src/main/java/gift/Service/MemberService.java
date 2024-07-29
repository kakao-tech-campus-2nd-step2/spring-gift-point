package gift.Service;

import gift.Exception.ForbiddenException;
import gift.Exception.MemberNotFoundException;
import gift.Model.Entity.Member;
import gift.DTO.RequestMemberDTO;
import gift.Model.Value.Email;
import gift.Repository.MemberRepository;
import gift.Util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;


    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil){
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public Member signUpUser(RequestMemberDTO requestMemberDTO){
        return memberRepository.save(new Member(requestMemberDTO.email(), requestMemberDTO.password()));
    }

    @Transactional(readOnly = true)
    public String loginUser(RequestMemberDTO requestMemberDTO) throws ForbiddenException {
        Member member = memberRepository.findByEmail(new Email(requestMemberDTO.email())).orElseThrow(() -> new MemberNotFoundException("매칭되는 멤버가 없습니다."));
        String temp = member.getPassword().getValue();
        if (!(temp.equals(requestMemberDTO.password())))
            throw new ForbiddenException("잘못된 로그인입니다");

        return jwtUtil.generateToken(member);
    }

    @Transactional(readOnly = true)
    public Member getUserByToken(String token) {
        return memberRepository.findByEmail(new Email(jwtUtil.getSubject(token))).orElseThrow(()-> new MemberNotFoundException("매칭되는 멤버가 없습니다"));
    }
}
