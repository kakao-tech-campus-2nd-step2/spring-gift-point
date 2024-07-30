package gift.client;

import gift.exception.ApiRequestException;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Component
public class KakaoClient {

    private final RestTemplate restTemplate;

    public KakaoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(errorHandler());
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
}