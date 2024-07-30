package gift.member.service;

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

    public MemberServiceImpl(MemberRepository memberRepository, @Value("${jwt.secret}") String secretKey) {
        this.memberRepository = memberRepository;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public void registerMember(String email, String password) {
        Member member = new Member(email, password);
        Optional<Member> existingMember = memberRepository.findByEmail(email);

        // validation
        if (existingMember.isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        memberRepository.save(member);
    }

    public String generateToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())  // 이메일 클레임 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1시간 만료
                .signWith(key)
                .compact();
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("No such member: ")
        );

        if (!member.getPassword().equals(password)) {
            return null;
        }

        return generateToken(member);
    }
}
