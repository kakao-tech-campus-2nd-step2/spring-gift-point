package gift.config;

import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient(ClientHttpRequestFactory clientHttpRequestFactory) {
        return RestClient.builder().
                requestFactory(clientHttpRequestFactory).
                build();
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        // Apache Http Client 5의 default retry 설정은 3회 재시도
        // 직접 requestFactory를 생성하여 retry 설정을 4회로 변경
        final int maxRetryAttempts = 4;
        final long retryInterval = 300L;

        CloseableHttpClient httpClient = HttpClients.custom()
                .setRetryStrategy(
                        new DefaultHttpRequestRetryStrategy(maxRetryAttempts, TimeValue.ofMilliseconds(retryInterval)))
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
