package gift.service;


import gift.dto.LoginResponseDTO;
import gift.dto.MemberRequestDTO;
import gift.dto.MemberResponseDTO;
import gift.exception.DuplicateException;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    // 회원 가입 메서드
    public void register(MemberRequestDTO memberRequestDTO) {
        if (memberRepository.findByEmail(memberRequestDTO.getEmail()).isPresent()) {
            throw new DuplicateException("이미 존재하는 회원입니다.");
        }
        Member member = memberRequestDTO.toEntity();
        memberRepository.save(member);
    }

    // 사용자 인증 메서드
    public LoginResponseDTO authenticate(MemberRequestDTO memberRequestDTO) {
        Optional<Member> memberOpt = memberRepository.findByEmail(memberRequestDTO.getEmail());
        if (memberOpt.isEmpty() || !memberOpt.get().checkPassword(memberRequestDTO.getPassword())) {
            throw new IllegalArgumentException("유효하지 않은 이메일 or 비밀번호입니다.");
        }
        String token = JwtUtil.generateToken(memberRequestDTO.getEmail());
        return new LoginResponseDTO(token);
    }

    // 토큰 이용하여 사용자 조회하는 메서드
    public MemberResponseDTO findByToken(MemberRequestDTO memberRequestDTO) {
        String email = JwtUtil.extractEmail(memberRequestDTO.getToken());
        if (!JwtUtil.validateToken(memberRequestDTO.getToken(), email)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return MemberResponseDTO.fromEntity(member);
    }

    // 이메일 이용하여 사용자 조회하는 메서드
    public MemberResponseDTO findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return MemberResponseDTO.fromEntity(member);
    }

    // 이메일 이용하여 사용자 엔티티 조회 메서드
    public Member findMemberEntityByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    // 토큰에서 이메일 추출
    public String extractEmailFromToken(String token) {
        return JwtUtil.extractEmail(token);
    }
}
