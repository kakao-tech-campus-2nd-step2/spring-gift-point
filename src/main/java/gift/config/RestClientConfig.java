package gift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.Builder;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(Builder builder) {
        return builder
            .requestFactory(getClientHttpRequestFactory())
            .build();
    }

    public ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(1000);
        clientHttpRequestFactory.setConnectionRequestTimeout(1000);
        return clientHttpRequestFactory;
    }
}
