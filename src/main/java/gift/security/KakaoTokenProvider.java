package gift.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.Kakao.KakaoProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class KakaoTokenProvider {
    private final String tokenUrl;
    private final RestClient client = RestClient.builder().build();
    private final KakaoProperties kakaoProperties;

    public KakaoTokenProvider(@Value("${kakao.tokenUrl}") String tokenUrl, KakaoProperties kakaoProperties) {
        this.tokenUrl = tokenUrl;
        this.kakaoProperties = kakaoProperties;
    }
    /*
     * 토큰 발급
     */
    public String getToken(String code) throws JsonProcessingException {
        LinkedMultiValueMap<Object, Object> body = makeBody(code);

        ResponseEntity<String> entity = client.post()
                .uri(URI.create(tokenUrl))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        String resBody = entity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(resBody);

        return jsonNode.get("access_token").asText();
    }
    /*
     * 토큰 요청을 위한 Body 만들기
     */
    private LinkedMultiValueMap<Object, Object> makeBody(String code){
        var body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_url", kakaoProperties.getRedirectUrl());
        body.add("code", code);

        return body;
    }
}
