package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@ConfigurationProperties(prefix = "kakao.message")
public record KakaoMessageConfig(
        String messageUrl
) {
    public RestClient createMessageSendClient(String accessToken) {
        return RestClient.builder()
                .baseUrl(messageUrl)
                .defaultHeader("content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader("authorization", accessToken)
                .build();
    }
}
