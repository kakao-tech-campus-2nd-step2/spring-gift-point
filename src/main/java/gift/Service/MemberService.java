package gift.Service;

import gift.Exception.EmailAlreadyExistsException;
import gift.Exception.ForbiddenException;
import gift.Exception.MemberNotFoundException;
import gift.Model.Entity.Member;
import gift.DTO.RequestMemberDTO;
import gift.Model.Value.Email;
import gift.Model.Value.Point;
import gift.Repository.MemberRepository;
import gift.Util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;


    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil){
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String signUpUser(RequestMemberDTO requestMemberDTO){
        Optional<Member> optionalMember = memberRepository.findByEmail(new Email(requestMemberDTO.email()));
        if(optionalMember.isPresent())
            throw new EmailAlreadyExistsException("이미 존재하는 이메일입니다");
        Member member =  memberRepository.save(new Member(requestMemberDTO.email(), requestMemberDTO.password(), 0));
        return jwtUtil.generateToken(member);
    }

    @Transactional(readOnly = true)
    public String loginUser(RequestMemberDTO requestMemberDTO) throws ForbiddenException {
        Member member = memberRepository.findByEmail(new Email(requestMemberDTO.email())).orElseThrow(() -> new ForbiddenException("아이디가 존재하지 않습니다"));
        String temp = member.getPassword().getValue();
        if (!(temp.equals(requestMemberDTO.password())))
            throw new ForbiddenException("비밀번호가 틀렸습니다");

        return jwtUtil.generateToken(member);
    }

    @Transactional(readOnly = true)
    public Member getUserByToken(String token) {
        return memberRepository.findByEmail(new Email(jwtUtil.getSubject(token))).orElseThrow(()-> new MemberNotFoundException("매칭되는 멤버가 없습니다"));
    }
}
