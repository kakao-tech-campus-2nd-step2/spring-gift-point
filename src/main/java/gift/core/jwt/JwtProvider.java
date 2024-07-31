package gift.core.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import gift.user.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
public class JwtProvider {
	private final String secret = "gift-secret-key-yooonwodyd-2024-07-05-2024-12-31";
	private static final String HEADER_PREFIX = "Bearer ";
	private static final long TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; // 7일
	private Key secretKey;

	// SecretKey 초기화
	@PostConstruct
	public void init() {
		secretKey = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
	}

	// And로 목적
	public Claims getClaims(String rawToken) {
		var token = parseHeader(rawToken);
		return extractClaims(token);
	}

	public String parseHeader(String header) {
		if (header == null || header.isEmpty()) {
			throw new IllegalArgumentException("Authorization 헤더가 없습니다.");
		} else if (!header.startsWith(HEADER_PREFIX)) {
			throw new IllegalArgumentException("Authorization 올바르지 않습니다. Bearer로 시작해야합니다.");
		} else if (header.split(" ").length != 2) {
			throw new IllegalArgumentException("Authorization 올바르지 않습니다.");
		}

		return header.split(" ")[1];
	}

	// 토큰 생성, ROLE을 담아서 반환
	public String generateToken(String userId, Set<Role> roles) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + TOKEN_VALIDITY);

		// roles를 ,를 구분자로 스트링으로 변환
		return Jwts.builder()
			.setSubject(userId)
			.claim("roles", roles.stream()
				.map(Role::name)
				.collect(Collectors.joining(",")))
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	private Claims extractClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			throw new JwtException("토큰이 만료되었습니다.");
		}
	}
}
