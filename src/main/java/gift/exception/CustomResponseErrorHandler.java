package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() ||
                response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String seriesName = "UNKNOWN_ERROR";

        HttpStatus statusCode = (HttpStatus) response.getStatusCode();
        if (statusCode.series() == HttpStatus.Series.SERVER_ERROR) {
            seriesName = HttpStatus.Series.SERVER_ERROR.name();
            throw new HttpServerErrorException(statusCode, "Server Error: " + seriesName);
        } else if (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) {
            if(response.getStatusCode() == HttpStatus.NOT_FOUND) {
                seriesName = HttpStatus.NOT_FOUND.name();
                throw new HttpClientErrorException(statusCode, "Not Found" + seriesName);
            }
            seriesName = HttpStatus.Series.CLIENT_ERROR.name();
            throw new HttpClientErrorException(statusCode, "Client Error" + seriesName);
        }
    }
}
