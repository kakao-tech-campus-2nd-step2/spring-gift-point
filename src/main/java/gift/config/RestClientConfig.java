package gift.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${api.timeout}")
    private int timeout;

    private final HttpRestClientResponseErrorHandler errorHandler = new HttpRestClientResponseErrorHandler();

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
            .requestFactory(getClientHttpRequestFactory())
            .defaultStatusHandler(errorHandler)
            .build();
    }

    public ClientHttpRequestFactory getClientHttpRequestFactory() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(timeout));
        factory.setReadTimeout(Duration.ofSeconds(timeout));
        return factory;
    }

}