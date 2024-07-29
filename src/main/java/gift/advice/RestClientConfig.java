package gift.advice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${app.gateway.connection-timeout}")
    private Integer connectionTimeout;

    @Value("${app.gateway.request-timeout}")
    private Integer requestTimeout;

    @Bean
    public RestClient restClient() {
        return RestClient.builder().requestFactory(clientHttpRequestFactory()).build();
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(connectionTimeout);
        clientHttpRequestFactory.setConnectionRequestTimeout(requestTimeout);
        return clientHttpRequestFactory;
    }
}
