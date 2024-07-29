package gift.product.util;

import static gift.product.exception.GlobalExceptionHandler.INVALID_TOKEN;
import static gift.product.exception.GlobalExceptionHandler.NOT_FOUND_MEMBER;

import gift.product.exception.InvalidIdException;
import gift.product.model.Member;
import gift.product.repository.MemberRepository;
import gift.product.repository.SnsMemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final MemberRepository memberRepository;

    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private final Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
    private final SnsMemberRepository snsMemberRepository;

    @Autowired
    public JwtUtil(
        MemberRepository memberRepository,
        SnsMemberRepository snsMemberRepository
    ) {
        this.memberRepository = memberRepository;
        this.snsMemberRepository = snsMemberRepository;
    }

    // 토큰 생성
    public String generateToken(String email) {
        System.out.println("[JwtUtil] generateToken()");
        long expirationTimeMillis = 1000 * 60 * 60; // 밀리초 -> 초 -> 분 -> 시간 (단위 변경, default=ms)
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expirationTimeMillis);
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    // 토큰의 서명 및 유효성 검증
    public boolean isValidToken(String token) {
        System.out.println("[JwtUtil] isValidToken()");
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT token is invalid: " + e.getMessage());
        }
        return false;
    }

    // 토큰에서 클레임 추출
    public Claims extractClaims(String token) {
        System.out.println("[JwtUtil] extractClaims()");
        return Jwts.parser()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    
    // 토큰으로 신원 인증한 사용자 정보 반환
    public Member parsingToken(String authorization) {
        String token = authorization.substring(7);
        String email = extractClaims(token).getSubject();
        if(!email.contains("@")) {
            Long snsMemberId = snsMemberRepository.findByKakaoId(Long.valueOf(email))
                .orElseThrow(() -> new InvalidIdException(INVALID_TOKEN))
                .getId();
            return memberRepository.findBySnsMemberId(snsMemberId)
                .orElseThrow(() -> new InvalidIdException(NOT_FOUND_MEMBER));
        }
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidIdException(INVALID_TOKEN));
    }
}
