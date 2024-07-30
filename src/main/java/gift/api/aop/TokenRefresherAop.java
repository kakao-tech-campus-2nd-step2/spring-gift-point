package gift.api.aop;

import gift.member.oauth.OauthService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TokenRefresherAop {

    private final OauthService oauthService;

    public TokenRefresherAop(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @Before("@annotation(gift.api.aop.TokenRefresher) && args(accessToken, ..)")
    public void refreshAccessToken(JoinPoint joinPoint, String accessToken) {
        oauthService.verifyToken(accessToken);
    }
}
