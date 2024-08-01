package gift.web;

import gift.web.exception.KakaoRestClientErrHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    private final KakaoRestClientErrHandler kakaoRestClientErrHandler;

    public RestClientConfig(KakaoRestClientErrHandler kakaoRestClientErrHandler) {
        this.kakaoRestClientErrHandler = kakaoRestClientErrHandler;
    }

    @Bean
    public RestClient restClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        return RestClient.builder()
            .requestFactory(factory)
            .defaultStatusHandler(kakaoRestClientErrHandler)
            .build();
    }
}
