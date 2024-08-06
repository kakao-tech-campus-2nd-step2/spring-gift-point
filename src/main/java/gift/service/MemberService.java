package gift.service;

import gift.dto.KakaoTokenResponseDTO;
import gift.dto.KakaoUserDTO;
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
    public LoginResponseDTO register(MemberRequestDTO memberRequestDTO) {
        // 이미 존재하는 회원인지 확인
        if (memberRepository.findByEmail(memberRequestDTO.getEmail()).isPresent()) {
            throw new DuplicateException("이미 존재하는 회원입니다.");
        }
        // 새로운 회원 생성 및 저장
        Member member = new Member(memberRequestDTO.getEmail(), memberRequestDTO.getPassword());
        memberRepository.save(member);

        // 토큰 생성 및 반환
        String token = JwtUtil.generateToken(member.getEmail());
        return new LoginResponseDTO(token, member.getEmail());
    }

    // 사용자 인증 메서드
    public LoginResponseDTO authenticate(MemberRequestDTO memberRequestDTO) {
        Optional<Member> memberOpt = memberRepository.findByEmail(memberRequestDTO.getEmail());
        if (memberOpt.isEmpty() || !memberOpt.get().checkPassword(memberRequestDTO.getPassword())) {
            throw new IllegalArgumentException("유효하지 않은 이메일 or 비밀번호입니다.");
        }
        String token = JwtUtil.generateToken(memberRequestDTO.getEmail());
        return new LoginResponseDTO(token, memberRequestDTO.getEmail());
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

    // ID 이용하여 사용자 엔티티 조회 메서드 추가
    public Member findMemberEntityById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    // 포인트 조회 메서드
    public int getMemberPoints(String email) {
        Member member = findMemberEntityByEmail(email);
        return member.getPoints();
    }

    // 포인트 업데이트 메서드
    public void updateMemberPoints(String email, int points) {
        Member member = findMemberEntityByEmail(email);
        member.setPoints(points);
        memberRepository.save(member);
    }

    // 토큰에서 이메일 추출
    public String extractEmailFromToken(String token) {
        return JwtUtil.extractEmail(token);
    }

    // 토큰 검증 메서드
    public void validateToken(String token, Long memberId) {
        String email = JwtUtil.extractEmail(token);
        Member member = findMemberEntityById(memberId);
        if (!JwtUtil.validateToken(token, email) || !member.getEmail().equals(email)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }
}

