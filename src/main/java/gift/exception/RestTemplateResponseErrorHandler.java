package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static gift.exception.errorMessage.Messages.*;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is5xxServerError() ||
                httpResponse.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatus statusCode = HttpStatus.valueOf(httpResponse.getStatusCode().value());

        if (statusCode.is4xxClientError()) {
            handleClientError(httpResponse);
        } else if (statusCode.is5xxServerError()) {
            handleServerError(httpResponse);
        }
    }

    private void handleClientError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatus statusCode = HttpStatus.valueOf(httpResponse.getStatusCode().value());

        switch (statusCode) {
            case BAD_REQUEST:
                throw new HttpClientErrorException(statusCode, API_BAD_REQUEST);
            case UNAUTHORIZED:
                throw new HttpClientErrorException(statusCode, API_UNAUTHORIZED);
            case FORBIDDEN:
                throw new HttpClientErrorException(statusCode, API_FORBIDDEN);
            case TOO_MANY_REQUESTS:
                throw new HttpClientErrorException(statusCode, API_TOO_MANY_REQUESTS);
            default:
        }
    }

    private void handleServerError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatus statusCode = HttpStatus.valueOf(httpResponse.getStatusCode().value());

        switch (statusCode) {
            case INTERNAL_SERVER_ERROR:
                throw new HttpServerErrorException(statusCode, API_INTERNAL_SERVER_ERROR);
            case BAD_GATEWAY:
                throw new HttpServerErrorException(statusCode, API_BAD_GATEWAY);
            case SERVICE_UNAVAILABLE:
                throw new HttpServerErrorException(statusCode, API_SERVICE_UNAVAILABLE);
            default:
        }
    }
}
