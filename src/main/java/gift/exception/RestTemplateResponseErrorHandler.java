package gift.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is4xxClientError() || httpResponse.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().is5xxServerError()) {
            throw new HttpClientErrorException(httpResponse.getStatusCode());
        }
        
        if (httpResponse.getStatusCode().is4xxClientError()) {
            if (httpResponse.getStatusCode() == HttpStatus.FORBIDDEN) {
            	throw new KakaoNotEnabledException("Forbidden: [spring-gift] App disabled [talk_message] scopes for [TALK_MEMO_DEFAULT_SEND] API on developers.kakao.com. Enable it first.");
            }
            throw new HttpClientErrorException(httpResponse.getStatusCode());
        }
    }
}
