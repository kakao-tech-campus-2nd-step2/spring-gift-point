package gift.config;

import java.util.concurrent.TimeUnit;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.Builder;

@Configuration
public class RestClientConfig {

    static final int READ_TIMEOUT = 1000;
    static final int CONNECTION_TIMEOUT = 1000;
    static final int CONNECTION_REQUEST_TIMEOUT = 1000;

    @Bean
    public RestClient restClient(Builder builder) {
        return builder
            .requestFactory(getClientHttpRequestFactory())
            .build();
    }

    public ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        clientHttpRequestFactory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
        clientHttpRequestFactory.setHttpClient(createHttpClient());

        return clientHttpRequestFactory;
    }


    private HttpClient createHttpClient() {
        return HttpClientBuilder.create()
            .setConnectionManager(createHttpClientConnectionManager())
            .build();
    }

    private HttpClientConnectionManager createHttpClientConnectionManager() {
        return PoolingHttpClientConnectionManagerBuilder.create()
            .setDefaultConnectionConfig(ConnectionConfig.custom()
                .setSocketTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .build())
            .build();
    }
}
