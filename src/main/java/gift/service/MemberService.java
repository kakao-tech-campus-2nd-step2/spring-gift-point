package gift.service;

import gift.dto.MemberDto;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtility;
import gift.util.TokenBlacklist;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenBlacklist tokenBlacklist;

    public MemberService(MemberRepository memberRepository, TokenBlacklist tokenBlacklist) {
        this.memberRepository = memberRepository;
        this.tokenBlacklist = tokenBlacklist;
    }

    public String register(@Valid MemberDto memberDto) {
        validateNewMember(memberDto);
        String token = JwtUtility.generateToken(memberDto.getEmail());
        Member member = new Member(memberDto.getEmail(), memberDto.getPassword(), token);
        memberRepository.save(member);
        return token;
    }

    private void validateNewMember(MemberDto memberDto) {
        memberRepository.findByEmail(memberDto.getEmail())
                .ifPresent(existingMember -> {
                    throw new DuplicateKeyException("이미 존재하는 이메일입니다.");
                });
    }

    public String login(MemberDto memberDto) {
        Member existingMember = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 이메일 또는 잘못된 비밀번호입니다."));
        if (!existingMember.getPassword().equals(memberDto.getPassword())) {
            throw new NoSuchElementException("존재하지 않는 이메일 또는 잘못된 비밀번호입니다.");
        }
        String token = JwtUtility.generateToken(existingMember.getEmail());
        Member updatedMember = new Member(existingMember, token);
        memberRepository.save(updatedMember);
        return token;
    }

    public void logout(String token) {
        Member member = memberRepository.findByActiveToken(token)
                .orElseThrow(() -> new NoSuchElementException("유효하지 않은 토큰입니다."));
        Member updatedMember = new Member(member);
        memberRepository.save(updatedMember);
        tokenBlacklist.add(token);
    }

    public Member findByActiveToken(String token) {
        return memberRepository.findByActiveToken(token)
                .orElseThrow(() -> new NoSuchElementException("유효하지 않은 토큰입니다."));
    }

    public void isTokenBlacklisted(String token) {
        if (tokenBlacklist.contains(token)) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 토큰입니다");
        }
    }
}
