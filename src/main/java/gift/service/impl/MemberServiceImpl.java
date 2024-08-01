package gift.service.impl;

import gift.model.Member;
import gift.repository.MemberRepository;
import gift.service.MemberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Optional<String> registerMember(Member member) {
        String hashedPassword = BCrypt.hashpw(member.getPassword(), BCrypt.gensalt());
        member.setPassword(hashedPassword);
        Member savedMember = memberRepository.save(member);

        String token = Jwts.builder()
            .subject(savedMember.getId().toString())
            .claim("email", savedMember.getEmail())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();

        return Optional.of(token);
    }

    @Override
    public Optional<String> login(String email, String password) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            if (BCrypt.checkpw(password, member.getPassword())) {
                String jws = Jwts.builder()
                    .subject(member.getId().toString())
                    .claim("email", member.getEmail())
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .compact();
                return Optional.of(jws);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }
}
