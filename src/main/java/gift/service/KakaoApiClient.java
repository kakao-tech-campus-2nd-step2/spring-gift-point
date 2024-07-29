package gift.service;

import gift.dto.KakaoUserInfo;
import java.net.URI;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoApiClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String KAKAO_API_URI = "https://kapi.kakao.com";

    public KakaoUserInfo getUserInfo(String accessToken) {
        String url = KAKAO_API_URI + "/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(URI.create(url),
                HttpMethod.GET, request, KakaoUserInfo.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Error while fetching user info", e);
        }
    }

    public void sendOrderMessage(String token, String messageTemplate) {
        String url = KAKAO_API_URI + "/v2/api/talk/memo/default/send";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", messageTemplate);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            System.out.println("카카오톡 메시지 전송 실패. 상태 코드: " + response.getStatusCode());
        } else {
            System.out.println("카카오톡 메시지가 성공적으로 전송되었습니다!");
        }
    }

}
