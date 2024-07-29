package gift.config;

import gift.exception.customException.KakaoApiException;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class HttpRestClientResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() || response.getStatusCode()
            .is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String message = response.getBody().toString();
        if (response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
            message = response.getHeaders().getFirst("WWW-Authenticate");
        }
        HttpStatusCode code = response.getStatusCode();
        throw new KakaoApiException(code, message);
    }
}