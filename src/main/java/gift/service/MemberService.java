package gift.service;


import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;


import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }


    public void generateUser(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.MEMBER_EMAIL_DUPLICATED);
        }
        memberRepository.save(member);

    }

    public String authenticateUser(Member member) {
        Member loginMember = memberRepository.findByEmailAndPassword(member.getEmail(),
            member.getPassword());

        if (loginMember == null) {
            throw new CustomException(ErrorCode.MEMBER_LOGIN_NOT_ALLOWED);
        }
        return jwtUtil.generateToken(loginMember.getEmail());

    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }


}
