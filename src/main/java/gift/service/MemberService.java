package gift.service;

import gift.entity.Member;
import gift.exception.LoginException;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public String registerMember(String email, String password) {
        if (memberRepository.existsByEmail(email)) {
            throw new LoginException("이미 존재하는 이메일 입니다.", HttpStatus.CONFLICT);
        }
        Member member = new Member(email, password);
        memberRepository.save(member);
        return JwtUtil.generateToken(email);
    }

    public String authenticateMember(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new LoginException(
                "이메일 혹은 비밀번호가 불일치 합니다.", HttpStatus.FORBIDDEN));
        return JwtUtil.generateToken(email);
    }

    public Long getMemberIdByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new LoginException("해당 이메일로 등록된 사용자 정보가 없습니다.", HttpStatus.NOT_FOUND));
        return member.getId();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(
                () -> new LoginException("해당 아이디로 등록된 사용자 정보가 없습니다.", HttpStatus.NOT_FOUND));
    }
}