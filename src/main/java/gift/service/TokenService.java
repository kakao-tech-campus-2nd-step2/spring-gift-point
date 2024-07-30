package gift.service;

import gift.domain.Member;
import gift.domain.TokenAuth;
import gift.exception.UnAuthorizationException;
import gift.repository.token.TokenSpringDataJpaRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static gift.exception.ErrorCode.UNAUTHORIZED;


@Service
@Transactional(readOnly = true)
public class TokenService {

    private final TokenSpringDataJpaRepository tokenRepository;

    private final String secretKey;

    public TokenService(TokenSpringDataJpaRepository tokenRepository, @Value("${jwt.secret-key}") String secretKey) {
        this.tokenRepository = tokenRepository;
        this.secretKey = secretKey;
    }

    @Transactional
    public String saveToken(Member member) {
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())
                .signWith(getSecretKey())
                .compact();

       return saveToken(member, accessToken);
    }

    @Transactional
    public String saveToken(Member member, String accessToken) {
        TokenAuth tokenAuth = tokenRepository.findByMember(member)
                .orElse(new TokenAuth());

        tokenAuth.setMember(member);
        tokenAuth.setToken(accessToken);

        tokenRepository.save(tokenAuth);

        return tokenAuth.getToken();
    }

    public TokenAuth findToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new UnAuthorizationException(UNAUTHORIZED));
    }

//    public String getMemberIdFromToken(String token) {
//        Claims claims = parseToken(token);
//        return claims.getSubject();
//    }
//
//    public Claims parseToken(String token) {
//        JwtParser parser = Jwts.parserBuilder()
//                .setSigningKey(getSecretKey())
//                .build();
//        return parser.parseClaimsJws(token).getBody();
//    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

}

