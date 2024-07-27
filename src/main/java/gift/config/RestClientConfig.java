package gift.config;

import java.util.concurrent.TimeUnit;
import org.apache.hc.client5.http.config.RequestConfig;
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
        final int connectTimeout = 5000; // 연결 시간 초과(서버에 연결을 시도할 때까지 기다리는 시간)
        final int socketTimeout = 5000; // 응답 시간 초과
        final int connectionRequestTimeout = 1000; // 연결 요청 시간 초과 (연결 풀에서 연결을 얻기 위해 기다리는 최대 시간)

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .setResponseTimeout(socketTimeout, TimeUnit.MILLISECONDS)
                .setConnectionRequestTimeout(connectionRequestTimeout, TimeUnit.MILLISECONDS)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setRetryStrategy(
                        new DefaultHttpRequestRetryStrategy(maxRetryAttempts, TimeValue.ofMilliseconds(retryInterval)))
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
