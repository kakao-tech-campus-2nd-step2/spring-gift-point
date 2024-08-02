package gift.member.business.service;

import gift.member.business.dto.JwtToken;
import gift.global.authentication.jwt.JwtValidator;
import gift.global.authentication.jwt.TokenType;
import gift.global.exception.ErrorCode;
import gift.global.exception.custrom.LoginException;
import gift.member.business.dto.MemberIn;
import gift.member.persistence.entity.Member;
import gift.member.persistence.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtValidator jwtValidator;

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;

    public MemberService(
        @Value("${spring.jwt.access_expiration}") long accessExpireTime,
        @Value("${spring.jwt.refresh_expiration}") long refreshExpireTime,
        MemberRepository memberRepository,
        JwtValidator jwtValidator) {
        this.ACCESS_TOKEN_EXPIRE_TIME = accessExpireTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshExpireTime;
        this.memberRepository = memberRepository;
        this.jwtValidator = jwtValidator;
    }

    public String registerMember(MemberIn.Register MemberInRegister) {
        var member = MemberInRegister.toMember();
        var id = memberRepository.saveMember(member);
        return createToken(id).accessToken();
    }

    public String loginMember(MemberIn.Login MemberInLogin) {
        var member = memberRepository.getMemberByEmail(MemberInLogin.email());
        if (!member.getPassword().equals(MemberInLogin.password())) {
            throw new LoginException(ErrorCode.LOGIN_ERROR, "패스워드가 이메일과 일치하지 않습니다.");
        }
        return createToken(member.getId()).accessToken();
    }

    public String registerVendorMember(MemberIn.VendorRegister memberInRegister) {
        var member = memberInRegister.toMember();
        var id = memberRepository.saveMember(member);
        return createToken(id).accessToken();
    }

    public String loginVendorMember(MemberIn.VendorLogin memberInLogin) {
        var member = memberRepository.getMemberByEmail(memberInLogin.email());
        if (member.getOAuthProvider() != memberInLogin.oAuthProvider()) {
            throw new LoginException(ErrorCode.LOGIN_ERROR, "OAuthProvider가 일치하지 않습니다.");
        }
        member.updateToken(memberInLogin.accessToken(), memberInLogin.refreshToken());
        return createToken(member.getId()).accessToken();
    }

    public String reissueAccessToken(String refreshToken) {
        Long id = jwtValidator.validateAndParseToken(refreshToken, TokenType.REFRESH);
        long current = System.currentTimeMillis();
        var accessExpireTime = new Date(current + ACCESS_TOKEN_EXPIRE_TIME);
        return generateToken(id, accessExpireTime, Map.of("type", TokenType.ACCESS));
    }

    public Member getMemberById(Long memberId) {
        return memberRepository.getMemberById(memberId);
    }

    private JwtToken createToken(Long id) {
        long current = System.currentTimeMillis();
        var accessExpireTime = new Date(current + ACCESS_TOKEN_EXPIRE_TIME);
        var refreshExpireTime = new Date(current + REFRESH_TOKEN_EXPIRE_TIME);
        var accessToken = generateToken(id, accessExpireTime, Map.of("type", TokenType.ACCESS));
        var refreshToken = generateToken(id, refreshExpireTime, Map.of("type", TokenType.REFRESH));
        return new JwtToken(accessToken, refreshToken);
    }

    private String generateToken(Long id, Date expireTime, Map<String, Object> claims) {
        SecretKey secretKey = jwtValidator.secretKey();

        return Jwts.builder()
            .claims(claims)
            .subject(id.toString())
            .expiration(expireTime)
            .signWith(secretKey)
            .compact();
    }
}
