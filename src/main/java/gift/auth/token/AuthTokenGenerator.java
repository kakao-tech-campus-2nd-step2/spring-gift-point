package gift.auth.token;

import gift.member.dto.MemberResDto;
import gift.utils.JwtTokenProvider;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenGenerator {

    private static final String GRANT_TYPE = "Bearer";
    private static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30; // 30분
    private static final Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    private final JwtTokenProvider jwtTokenProvider;

    public AuthTokenGenerator(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthToken generateToken(MemberResDto memberResDto) {
        Long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = jwtTokenProvider.generateToken(memberResDto, accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.generateToken(memberResDto, refreshTokenExpiredAt);

        return new AuthToken(accessToken, refreshToken, GRANT_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);  // 만료 기간을 초 단위로 변환
    }

    public Long extractMemberId(String header) {
        String token = jwtTokenProvider.getToken(header);
        return jwtTokenProvider.getMemberId(token);
    }
}
