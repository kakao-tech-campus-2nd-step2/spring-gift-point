package gift.config;

import gift.exception.KakaoLoginException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        // HTTP 응답 코드가 4xx 또는 5xx 에러인지 확인
        return httpResponse.getStatusCode().is5xxServerError() ||
                httpResponse.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        // HTTP 상태 코드에 따라 예외 처리
        if (httpResponse.getStatusCode().is5xxServerError()) {
            // 서버 오류 처리
            throw new HttpClientErrorException(httpResponse.getStatusCode());
        } else if (httpResponse.getStatusCode().is4xxClientError()) {
            // 클라이언트 오류 처리
            throw new KakaoLoginException("카카오 로그인 실패");
        }
    }
}