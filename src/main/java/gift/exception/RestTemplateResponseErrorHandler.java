package gift.exception;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is4xxClientError() ||
                httpResponse.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().is5xxServerError()) {
            // 5xx 서버 오류 처리
            throw new HttpClientErrorException(httpResponse.getStatusCode());
        } else if (httpResponse.getStatusCode().is4xxClientError()) {
            // 4xx 클라이언트 오류 처리
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                try {
                    throw new ChangeSetPersister.NotFoundException();
                } catch (ChangeSetPersister.NotFoundException e) {
                    throw new RuntimeException(e);
                }

            } else if (httpResponse.getStatusCode() == HttpStatus.FORBIDDEN) {
                // 403 Forbidden 오류 처리
                throw new ForbiddenException("카카오 메시지 전송 거부 " + httpResponse.getBody().toString());
            }
            throw new HttpClientErrorException(httpResponse.getStatusCode());
        }
    }
}
