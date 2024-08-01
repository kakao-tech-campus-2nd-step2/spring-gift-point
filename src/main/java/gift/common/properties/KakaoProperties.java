package gift.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
        String clientId,
        String adminRedirectUrl,
        String tokenUrl,
        String memberInfoUrl,
        String loginUrl,
        String adminLoginUrl,
        String logoutUrl,
        String refreshUrl,
        String selfMessageUrl
) {
    public String getKakaoLoginUrl(String redirectUrl) {
        return loginUrl + redirectUrl + "&client_id=" + clientId;
    }
}
