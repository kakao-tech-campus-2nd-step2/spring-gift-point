package gift.service;

import gift.dto.response.JwtResponse;
import gift.dto.response.KakaoTokenResponse;
import gift.repository.KakaoAccessTokenRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    private static final SecretKey KEY = Jwts.SIG.HS256.key().build();
    private static final int JWT_EXPIRATION_IN_MS = 1000 * 60 * 60 * 2;

    private final KakaoAccessTokenRepository kakaoAccessTokenRepository;

    public TokenService(KakaoAccessTokenRepository kakaoAccessTokenRepository) {
        this.kakaoAccessTokenRepository = kakaoAccessTokenRepository;
    }

    public JwtResponse generateJwt(Long registeredMemberId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_IN_MS);
        String tokenValue = Jwts.builder()
                .claim("memberId", registeredMemberId)
                .expiration(expiryDate)
                .signWith(KEY)
                .compact();
        return new JwtResponse(tokenValue);
    }

    public Long getMemberId(String tokenValue) {
        String resultOfString = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(tokenValue)
                .getPayload()
                .get("memberId")
                .toString();
        return Long.parseLong(resultOfString);
    }

    public boolean isValidateToken(String tokenValue) {
        try {
            Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(tokenValue);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public void saveKakaoAccessToken(Long memberId, KakaoTokenResponse kakaoToken) {
        kakaoAccessTokenRepository.saveAccessToken(memberId, kakaoToken.accessToken());
    }
}
