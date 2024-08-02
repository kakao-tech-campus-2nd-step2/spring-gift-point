package gift.service;

import gift.dto.memberDTO.LoginRequestDTO;
import gift.dto.memberDTO.RegisterRequestDTO;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private MemberRepository memberRepository;
    private JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String registerMember(RegisterRequestDTO registerRequestDTO) {
        Member existingMember = memberRepository.findByEmail(registerRequestDTO.email());
        if (existingMember != null) {
            throw new IllegalArgumentException("이메일이 이미 존재합니다.");
        }
        Member member = new Member(null, registerRequestDTO.email(), registerRequestDTO.password(),
            "user");
        memberRepository.save(member);

        return jwtUtil.generateToken(member.getEmail(), member.getPassword());
    }

    public String loginMember(LoginRequestDTO loginRequestDTO) {
        Member member = memberRepository.findByEmail(loginRequestDTO.email());
        if (member == null || !member.getPassword().equals(loginRequestDTO.password())) {
            throw new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다.");
        }

        return jwtUtil.generateToken(member.getEmail(), member.getPassword());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}