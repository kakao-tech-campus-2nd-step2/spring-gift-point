package gift.exception;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(httpResponse.getStatusCode().value());
        return (statusCode != null && (statusCode.is4xxClientError() || statusCode.is5xxServerError()));
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(httpResponse.getStatusCode().value());
        if (statusCode != null) {
            if (statusCode.is4xxClientError()) {
                switch (statusCode) {
                    case UNAUTHORIZED:
                        throw new HttpClientErrorException(statusCode, "Unauthorized: 유효한 인증 자격 증명이 없습니다.");
                    case FORBIDDEN:
                        throw new HttpClientErrorException(statusCode, "Forbidden: 권한이 없습니다.");
                    case NOT_FOUND:
                        throw new HttpClientErrorException(statusCode, "Not Found: 요청한 리소스를 찾을 수 없습니다.");
                    default:
                        throw new HttpClientErrorException(statusCode, "Client Error: 클라이언트 오류가 발생했습니다.");
                }
            } else if (statusCode.is5xxServerError()) {
                throw new HttpServerErrorException(statusCode, "Server Error: 서버 오류가 발생했습니다.");
            }
        }
    }
}

