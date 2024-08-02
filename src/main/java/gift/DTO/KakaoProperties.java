package gift.DTO;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
        String restApiKey,
        String redirectUrl
) {
}
