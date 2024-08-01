package gift.service;

import gift.domain.KakaoTokenResponseDTO;
import gift.entity.Member;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public String signUp(String str) {
        try {
            var email = decodeToEmail(str);
            var password = decodeToPassword(str);
            String token = getToken(email, password);

            var member = new Member(email, password, token);
            memberRepository.save(member);
            return token;
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Invalid email or password : " + "(Email " + decodeToEmail(str) + "), (Password " +  decodeToPassword(str) + ")");
        }
    }

    public String login(String str) {
        var email = decodeToEmail(str);
        var password = decodeToPassword(str);
        try {
            return authenticate(email, password);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid email or password : " + "(Email " + email + "), (Password " + password + ")", e);
        }
    }

    public void kakaoLogin(String email, String token) {
        try {
            memberRepository.save(new Member(email, token));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 이메일입니다.: " + email);
        } catch (Exception e) {
            throw new RuntimeException("로그인에 실패하였습니다." + email);
        }
    }

    public boolean isValidToken(String token) {
        try {
            return memberRepository.existsByToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    private String authenticate(String email, String password) {
        var token = getToken(email, password);
        if (!isValidToken(token)) {
            throw new IllegalArgumentException("No such email or password");
        }
        return token;
    }

    private String getToken(String email, String password){
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        String accessToken = Jwts.builder()
                .claim("email", email)
                .claim("password", password)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        return accessToken;
    }

    private String decodeToEmail(String str) {
        String base64Credentials = str.substring("Basic ".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] values = credentials.split(":", 2);
        return values[0];
    }

    private String decodeToPassword(String str) {
        String base64Credentials = str.substring("Basic ".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] values = credentials.split(":", 2);
        return values[1];
    }
}
