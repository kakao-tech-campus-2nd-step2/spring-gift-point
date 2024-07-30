package gift.config;

import gift.exception.ExternalApiException;
import gift.external.api.kakao.KakaoProperties;
import gift.external.api.kakao.client.KakaoApiClient;
import gift.external.api.kakao.client.KakaoAuthClient;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@ConfigurationPropertiesScan
public class RestClientConfig {

    private final KakaoProperties kakaoProperties;

    public RestClientConfig(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @Bean
    public KakaoApiClient kakaoApiClient(RestClient.Builder restClientBuilder) {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
            .withConnectTimeout(Duration.ofSeconds(5))
            .withReadTimeout(Duration.ofSeconds(5));
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactories.get(settings);

        RestClient restClient = restClientBuilder
            .baseUrl(kakaoProperties.apiBaseUrl())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                throw new ExternalApiException("error.oauth.response");
            })
            .requestFactory(requestFactory)
            .build();
        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build().createClient(KakaoApiClient.class);
    }

    @Bean
    public KakaoAuthClient kakaoAuthClient(RestClient.Builder restClientBuilder) {
        RestClient restClient = restClientBuilder
            .baseUrl(kakaoProperties.authBaseUrl())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                throw new ExternalApiException("error.oauth.response");
            })
            .build();
        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build().createClient(KakaoAuthClient.class);
    }
}
