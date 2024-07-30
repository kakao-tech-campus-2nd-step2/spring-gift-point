package gift.config;

import gift.exception.ApiRequestException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Configuration
public class AppConfig {

    private final AppConfigProperties appConfigProperties;

    public AppConfig(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.setErrorHandler(errorHandler());
        return restTemplate;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        // 운영 환경에서는 prod 타임아웃값 사용
        if (isProduction()) {
            factory.setConnectTimeout(appConfigProperties.getProdConnectTimeout());
            factory.setReadTimeout(appConfigProperties.getProdReadTimeout());
        } else { // 개발 환경에서는 dev 타임아웃값 사용
            factory.setConnectTimeout(appConfigProperties.getDevConnectTimeout());
            factory.setReadTimeout(appConfigProperties.getDevReadTimeout());
        }

        return factory;
    }

    private ResponseErrorHandler errorHandler() {
        return new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    throw new ApiRequestException("Bad Request");
                } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new ApiRequestException("Not Found");
                } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                    throw new ApiRequestException("Internal Server Error");
                } else {
                    super.handleError(response);
                }
            }
        };
    }

    private boolean isProduction() {
        // 운영 환경을 식별하는 로직 구현. 예를 들어, 특정 환경 변수를 통해 운영 환경 식별
        return "prod".equals(System.getenv("ENVIRONMENT"));
    }
}