package gift.util;

import gift.util.errorException.BaseHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestCall {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> ResponseEntity<T> apiCall(String url, HttpMethod method, HttpEntity<?> requestEntity,
        Class<T> responseType) {
        try {
            return restTemplate.exchange(url, method, requestEntity, responseType);
        } catch (HttpClientErrorException e) {
            // 4xx 에러 처리
            throw new BaseHandler(e.getStatusCode(), e.getStatusText());
        } catch (HttpServerErrorException e) {
            // 5xx 에러 처리
            throw new BaseHandler(e.getStatusCode(), e.getStatusText());
        }
    }
}
