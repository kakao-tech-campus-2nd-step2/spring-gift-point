package gift.jwt;

import gift.user.KakaoUserDTO;
import gift.user.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {

    private String secretKey = "";

    public String generateAccessToken(User user){
        Date now = new Date();
        String encodeString = user.getEmail() + ":" + user.getPassword();
        secretKey = Base64.getEncoder().encodeToString(encodeString.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("nickName", user.getNickname())
                .issuedAt(now).expiration(createExpiredDate(now))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String generateAccessToken(KakaoUserDTO kakaoUser){
        Date now = new Date();
        String encodeString = String.valueOf(kakaoUser.getId()).repeat(5);
        secretKey = Base64.getEncoder().encodeToString(encodeString.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(kakaoUser.getId().toString())
                .claim("accessToken", kakaoUser.getAccessToken())
                .claim("refreshToken", kakaoUser.getRefreshToken())
                .issuedAt(now).expiration(createExpiredDate(now))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }


    private Date createExpiredDate(Date now) {
        return new Date(now.getTime() + (60 * (60 * 1000)));
    }

    public String getClaims(String jwt) {
        String tokenFromHeader = getTokenFromHeader(jwt);
        try{
            return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))).build().parseSignedClaims(tokenFromHeader).getPayload().getSubject();
        }
        catch (SignatureException e){
            return null;
        }
    }

    public boolean isKakaoUser(String jwt){
        String tokenFromHeader = getTokenFromHeader(jwt);
        try{
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))).build().parseSignedClaims(tokenFromHeader).getPayload().get("accessToken").toString();
            return true;
        }
        catch (SignatureException e){
            return false;
        }
    }

    public String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    public boolean tokenValidCheck(String jwt) {
        try{
            String claims = getClaims(jwt);
            return true;
        }
        catch (ExpiredJwtException e){
            throw new JwtException("토큰이 만료되었습니다");
        }
        catch (JwtException e){
            throw new JwtException(e.getMessage());
        }
        catch (NullPointerException e){
            throw new JwtException("토큰이 비어있습니다");
        }
        catch (Exception e){
            throw new JwtException(e.getMessage());
        }
    }

}
