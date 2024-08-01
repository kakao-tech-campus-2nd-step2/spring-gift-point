package gift.service;

import gift.domain.member.Member;
import gift.domain.member.MemberRepository;
import gift.dto.MemberRegisterRequestDto;
import gift.dto.MemberRegisterResponseDto;
import gift.dto.MemberRequestDto;
import gift.exception.InvalidPasswordException;
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

    public MemberRegisterResponseDto signUpMember(MemberRegisterRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member member = new Member(requestDto.email(), requestDto.name(), requestDto.password(), 1);
        Member registered = memberRepository.save(member);
        return new MemberRegisterResponseDto(registered.getId(), registered.getEmail(), requestDto.name());
    }

    public String loginMember(MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 멤버 정보입니다."));

        if (!member.isMatch(requestDto.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.generateToken(member.getId(), member.getEmail());
    }
}
