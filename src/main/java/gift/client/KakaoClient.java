package gift.client;

import gift.exception.ApiRequestException;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class KakaoClient {

    private final RestTemplate restTemplate;
    private final Map<HttpStatus, String> errorMessages;

    public KakaoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(errorHandler());

        errorMessages = new HashMap<>();
        errorMessages.put(HttpStatus.BAD_REQUEST, "Bad Request");
        errorMessages.put(HttpStatus.NOT_FOUND, "Not Found");
        errorMessages.put(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    private ResponseErrorHandler errorHandler() {
        return new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = HttpStatus.valueOf(response.getStatusCode().value());
                String statusText = response.getStatusText();

                String errorMessage = errorMessages.get(statusCode);
                if (errorMessage != null) {
                    throw new ApiRequestException(errorMessage + ": " + statusText);
                } else {
                    super.handleError(response);
                }
            }
        };
    }
}