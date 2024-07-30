package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoAuthProperties;
import gift.domain.KakaoInfo;
import gift.dto.KakaoMessageDto;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {
    private final KakaoAuthProperties kakaoAuthProperties;
    private final RestClient client = RestClient.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoService(KakaoAuthProperties kakaoAuthProperties) {
        this.kakaoAuthProperties = kakaoAuthProperties;
    }

    public String getKakaoToken(String code) throws JsonProcessingException {
        String url = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoAuthProperties.getClientId());
        body.add("redirect_url", kakaoAuthProperties.getRedirectUri());
        body.add("code", code);

        ResponseEntity<String> response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body) //request
            .retrieve() //response
            .toEntity(String.class);

        // 응답 처리
        String responseBody = response.getBody();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    public KakaoInfo getKakaoInfo(String accessToken) throws JsonProcessingException {
        String url = "https://kapi.kakao.com/v2/user/me";

        MultiValueMap<String, String> kakaoUserInfoRequest = new LinkedMultiValueMap<>();

        ResponseEntity<String> response = client.post()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(kakaoUserInfoRequest) //request
            .retrieve() //response
            .toEntity(String.class);

        // 응답 처리
        String responseBody = response.getBody();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String email = id + "@email.com";
        String password = String.valueOf(id);

        return new KakaoInfo(id, email, password);
    }

    public void sendKakaoMessageToMe(String accessToken, KakaoMessageDto kakaoMessageDto) throws JsonProcessingException {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        String objectJson = objectMapper.writeValueAsString(kakaoMessageDto);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", objectJson);

        ResponseEntity<String> response = client.post()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body) //request
            .retrieve() //response
            .toEntity(String.class);

    }
}
