package gift.service;

import gift.common.exception.conflict.EmailAlreadyExistsException;
import gift.common.exception.forbidden.InvalidCredentialsException;
import gift.common.exception.unauthorized.TokenErrorException;
import gift.common.exception.unauthorized.TokenNotFoundException;
import gift.common.util.JwtUtil;
import gift.dto.MemberRequest;
import gift.entity.Member;
import gift.repository.MemberRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String register(MemberRequest memberRequest) {
        Optional<Member> existingMember = memberRepository.findByEmail(memberRequest.getEmail());
        if (existingMember.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        Member member = new Member(memberRequest.getEmail(), memberRequest.getPassword());
        memberRepository.save(member);

        return JwtUtil.createToken(member.getEmail());
    }

    public String login(String email, String password) {
        return memberRepository.findByEmail(email)
            .filter(member -> member.getPassword().equals(password))
            .map(member -> JwtUtil.createToken(member.getEmail()))
            .orElseThrow(InvalidCredentialsException::new);
    }

    public Member getMemberFromToken(String token) {
        try {
            String email = JwtUtil.extractEmail(token);
            return memberRepository.findByEmail(email)
                .orElseThrow(TokenNotFoundException::new);
        } catch (Exception e) {
            throw new TokenErrorException();
        }
    }

    public String processKakaoLogin(String kakaoEmail, String refreshToken) {
        Optional<Member> existingMember = findByEmail(kakaoEmail);

        Member member = findByEmail(kakaoEmail)
            .orElseGet(() -> new Member(kakaoEmail, ""));

        member.setRefreshToken(refreshToken);
        registerNewMember(member);

        return JwtUtil.createToken(member.getEmail());
    }

    // 신규 회원 등록
    public void registerNewMember(Member member) {
        memberRepository.save(member);
    }

    // 이메일로 멤버 찾기
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

}
