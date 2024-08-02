package gift.member.service;

import gift.common.auth.JwtUtil;
import gift.member.model.Member;
import gift.member.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.security.Key;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final Key key;
    private final JwtUtil jwtUtil;

    public MemberServiceImpl(MemberRepository memberRepository, @Value("${jwt.secret}") String secretKey, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtUtil = jwtUtil;
    }

    // 회원가입
    public void registerMember(String email, String password) {
        // 신규 회원은 3000포인트를 부여
        Member member = new Member(email, password, 3000L);
        Optional<Member> existingMember = memberRepository.findByEmail(email);

        // validation
        if (existingMember.isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        memberRepository.save(member);
    }

    // 토큰 생성
    public String generateToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())  // 이메일 클레임 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1시간 만료
                .signWith(key)
                .compact();
    }

    // 로그인
    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("No such member: ")
        );

        if (!member.getPassword().equals(password)) {
            return null;
        }

        return generateToken(member);
    }

    // 포인트 조회
    public Long getPoint(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("No such member: ")
        );

        return member.getPoints();
    }

    // 토큰으로 사용자 정보 조회하기
    public Member getMemberByToken(String token) {
        String email = jwtUtil.extractEmail(token);

        return memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("토큰값에 해당하는 사용자 없음!!!")
        );
    }
}
