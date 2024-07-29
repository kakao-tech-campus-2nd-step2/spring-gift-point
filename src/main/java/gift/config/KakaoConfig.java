package gift.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:auth.properties")
public class KakaoConfig {

    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String REQUEST_MEMBER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String GRANT_TYPE = "authorization_code";
    @Value("${kakao.app-key}")
    private String kakaoAppKey;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;


    public String getKakaoAppKey() {
        return kakaoAppKey;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public static String getGrantType() {
        return GRANT_TYPE;
    }

    public static String getTokenUrl() {
        return TOKEN_URL;
    }

    public static String getRequestMemberInfoUrl() {
        return REQUEST_MEMBER_INFO_URL;
    }
}
