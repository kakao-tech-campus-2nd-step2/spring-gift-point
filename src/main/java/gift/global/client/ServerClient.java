package gift.global.client;

import java.net.URI;
import java.util.function.Consumer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

// 외부 API 연동을 담당하는 클래스. 처음에는 플랫폼 별로 나눌까 생각했지만 하나로 만들 수 있을 것 같아서 하나로 하였습니다.
@Component
public class ServerClient {

    private final RestClient restClient;

    public ServerClient() {
        restClient = RestClient.builder().build();
    }

    // 외부 API의 페이지를 반환하는 메서드.
    // 에러가 있으면 알아서 카카오가 처리하는 view로 반환해줄 것.
    public String getPage(String url, MultiValueMap<String, String> params) {
        return "redirect:" + UriComponentsBuilder.fromHttpUrl(url)
            .queryParams(params)
            .build()
            .toUriString();
    }

    // 외부 API에 get으로 요청하는 메서드
    // 플랫폼 별로 반환하는 에러 메시지가 다를 것이므로 플랫폼 별로 예외를 상이하게 처리해야 함.
    // 그러나 외부 API를 요청하는 클래스는 하나이므로 throws 사용
    public <T> ResponseEntity<T> getRequest(String url,
        Consumer<HttpHeaders> headersConsumer, Class<T> responseType)
        throws HttpClientErrorException {
        return restClient.get().uri(URI.create(url))
            .headers(headersConsumer)
            .retrieve()
            .toEntity(responseType);
    }

    // 외부 API에 post로 요청하는 메서드
    public <T> ResponseEntity<T> postRequest(String url, MultiValueMap<String, String> body,
        Consumer<HttpHeaders> headersConsumer, Class<T> responseType)
        throws HttpClientErrorException {
        return restClient.post().uri(URI.create(url))
            .headers(headersConsumer)
            .body(body)
            .retrieve()
            .toEntity(responseType);
    }
}
