package gift.service;

import gift.entity.Member;
import gift.exception.BusinessException;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
        Member member = new Member(email, password);
        memberRepository.save(member);
        return JwtUtil.generateToken(email);
    }

    public String authenticateMember(String email, String password) {
        return JwtUtil.generateToken(email);
    }

    public Long getMemberIdByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("해당 이메일로 등록된 사용자 정보가 없습니다."));
        return member.getId();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new BusinessException("해당 id로 등록된 사용자 정보가 없습니다."));
    }
}