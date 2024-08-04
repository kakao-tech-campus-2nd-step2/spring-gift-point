package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtility;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponseDto register(@Valid MemberRequestDto memberRequestDto) {
        validateNewMember(memberRequestDto);
        String token = JwtUtility.generateToken(memberRequestDto.getEmail());
        Member member = new Member(memberRequestDto.getEmail(), memberRequestDto.getPassword(), null);
        memberRepository.save(member);
        return new MemberResponseDto(member.getEmail(), token);
    }

    private void validateNewMember(MemberRequestDto memberRequestDto) {
        memberRepository.findByEmail(memberRequestDto.getEmail())
                .ifPresent(existingMember -> {
                    throw new DuplicateKeyException("이미 존재하는 이메일입니다.");
                });
    }

    public MemberResponseDto login(MemberRequestDto memberRequestDto) {
        Member existingMember = memberRepository.findByEmail(memberRequestDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 이메일 또는 잘못된 비밀번호입니다."));
        if (!existingMember.getPassword().equals(memberRequestDto.getPassword())) {
            throw new NoSuchElementException("존재하지 않는 이메일 또는 잘못된 비밀번호입니다.");
        }
        String token = JwtUtility.generateToken(existingMember.getEmail());
        return new MemberResponseDto(existingMember.getEmail(), token);
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 이메일입니다."));
    }

    public int getMemberPoints(String email) {
        Member member = getMemberByEmail(email);
        return member.getPoint();
    }

    public void updateMemberPoints(String email, int points) {
        Member existingMember = getMemberByEmail(email);
        Member updatedMember = new Member(
                existingMember.getId(),
                existingMember.getEmail(),
                existingMember.getPassword(),
                existingMember.getActiveToken(),
                points
        );
        memberRepository.save(updatedMember);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
