package gift.service;

import gift.dto.MemberDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDto register(MemberDto memberDto) {
        Member member = new Member(memberDto.getEmail(), memberDto.getPassword());
        Member savedMember = memberRepository.save(member);
        String token = TokenService.generateToken(member.getEmail(), member.getPassword());
        return new MemberDto(savedMember.getId(), savedMember.getEmail(), savedMember.getPassword(), token);
    }

    public MemberDto login(String email, String password) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        Member member = memberOptional.orElseThrow(() -> new RuntimeException("잘못된 인증입니다."));

        if (member.checkPassword(password)) {
            String token = TokenService.generateToken(email, password);
            return new MemberDto(member.getId(), email, password, token);
        } else {
            throw new RuntimeException("잘못된 인증입니다.");
        }
    }

    public MemberDto validateToken(String token) {
        logger.info("Validating token: {}", token);
        String[] parts = TokenService.decodeToken(token);
        String email = parts[0];
        String password = parts[1];
        logger.info("Decoded token - email: {}, password: {}", email, password);

        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        Member member = memberOptional.orElseThrow(() -> new RuntimeException("유효하지 않은 토큰입니다."));

        if (member.checkPassword(password)) {
            logger.info("Token is valid for member: {}", email);
            return new MemberDto(member.getId(), email, password, token);
        } else {
            logger.error("Invalid token for member: {}", email);
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }
}