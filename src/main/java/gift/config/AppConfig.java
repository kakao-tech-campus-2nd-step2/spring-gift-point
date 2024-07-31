package gift.config;

import gift.exception.ApiRequestException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Configuration
public class AppConfig {

    private final AppConfigProperties appConfigProperties;
    private final Environment environment;

    public AppConfig(AppConfigProperties appConfigProperties, Environment environment) {
        this.appConfigProperties = appConfigProperties;
        this.environment = environment;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(clientHttpRequestFactory()));
        restTemplate.setErrorHandler(errorHandler());
        return restTemplate;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        Map<String, Integer> connectTimeouts = Map.of(
                "dev", appConfigProperties.getDevConnectTimeout(),
                "prod", appConfigProperties.getProdConnectTimeout()
        );

        Map<String, Integer> readTimeouts = Map.of(
                "dev", appConfigProperties.getDevReadTimeout(),
                "prod", appConfigProperties.getProdReadTimeout()
        );

        String activeProfile = environment.getActiveProfiles().length > 0 ? environment.getActiveProfiles()[0] : "default";

        factory.setConnectTimeout(connectTimeouts.getOrDefault(activeProfile, 10000));
        factory.setReadTimeout(readTimeouts.getOrDefault(activeProfile, 10000));

        return factory;
    }

    private ResponseErrorHandler errorHandler() {
        Map<HttpStatus, String> errorMessages = Map.of(
                HttpStatus.BAD_REQUEST, "Bad Request",
                HttpStatus.NOT_FOUND, "Not Found",
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"
        );

        return new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = HttpStatus.resolve(response.getStatusCode().value());
                String statusText = response.getStatusText();

                if (statusCode != null && errorMessages.containsKey(statusCode)) {
                    throw new ApiRequestException(errorMessages.get(statusCode) + ": " + statusText);
                } else {
                    super.handleError(response);
                }
            }
        };
    }
}