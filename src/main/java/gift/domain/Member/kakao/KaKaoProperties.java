package gift.domain.Member.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
public record KaKaoProperties(String clientId, String redirectUrl, String tempAccessKey) {

}
