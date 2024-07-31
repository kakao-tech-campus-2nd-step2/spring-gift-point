package gift.service;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class KakaoMessageService {

	private final KakaoAuthService kakaoAuthService;
	
	@Value("${kakao.message-url}")
    private String messageUrl;

	public KakaoMessageService(KakaoAuthService kakaoAuthService) {
		this.kakaoAuthService = kakaoAuthService;
	}

	public Map<String, String> sendMessage(String accessToken, String message) {
		RequestEntity<MultiValueMap<String, String>> request = messageRequest(accessToken, message);
        ResponseEntity<Map<String, String>> response = kakaoAuthService.errorHandling(request,
        		new ParameterizedTypeReference<Map<String, String>>() {});
        return response.getBody();
    }
	
	private RequestEntity<MultiValueMap<String, String>> messageRequest(String accessToken, String message) {
        String templateObject = createMessageTemplate(message);
        HttpHeaders headers = createHeaders(accessToken);
        MultiValueMap<String, String> body = createBody(templateObject);
        return new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(messageUrl));
    }

	private String createMessageTemplate(String message) {
		return String.format("{\"object_type\": \"text\","
				+ "\"text\": \"%s\","
				+ "\"link\": {\"web_url\":\"http://localhost:8080\","
				+ "\"mobile_web_url\":\"http://localhost:8080\"},"
				+ "\"button_title\":\"확인\"}", message);
	}

	private HttpHeaders createHeaders(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return headers;
	}

	private MultiValueMap<String, String> createBody(String templateObject) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("template_object", templateObject);
		return body;
	}
}
