package gift.web.exception;

import gift.web.exception.kakaoapi.KakaoClientException;
import gift.web.exception.kakaoapi.KakaoServerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class KakaoRestClientErrHandler implements ResponseErrorHandler {

    // 4xx 에러나 5xx 에러가 나타나면 true를 반환
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() ||
            response.getStatusCode().is5xxServerError();
    }

    // 실질적인 response의 body를 읽어서 처리하는 로직
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // 서버 오류
        if(response.getStatusCode().is5xxServerError()) {
            throw new KakaoServerException(response.getStatusCode(), getResponseBody(response));
        }
        // 클라이언트 오류
        if(response.getStatusCode().is4xxClientError()) {
            throw new KakaoClientException(response.getStatusCode(), getResponseBody(response));
        }
    }

    private String getResponseBody(ClientHttpResponse response) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }
}
