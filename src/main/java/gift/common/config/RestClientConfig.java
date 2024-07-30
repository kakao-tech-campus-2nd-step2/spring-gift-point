package gift.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Duration;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
            .withConnectTimeout(Duration.ofSeconds(2))
            .withReadTimeout(Duration.ofSeconds(2));
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactories.get(settings);

        ResponseErrorHandler responseErrorHandler = new ErrorHandler();
        return RestClient.builder()
            .defaultStatusHandler(responseErrorHandler)
            .requestFactory(requestFactory)
            .build();
    }

    private class ErrorHandler implements ResponseErrorHandler {

        public ErrorHandler() {
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode().is4xxClientError() ||
                response.getStatusCode().is5xxServerError();
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            if(response.getStatusCode().is4xxClientError()) {
                throw new RuntimeException("4xx대 에러 발생");
            }
            if(response.getStatusCode().is5xxServerError()) {
                throw new RuntimeException("5xx대 에러 발생");
            }
        }
    }
}
