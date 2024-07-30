package gift.Validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.Model.GetKakaoTokenInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoTokenValidator {
    //token으로 id값을 받아오는 메소드
    public static long validateToken(String token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v1/user/access_token_info", org.springframework.http.HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().value() != 200) {
            return -1;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        GetKakaoTokenInformation getKakaoTokenInformation = objectMapper.readValue(response.getBody(), GetKakaoTokenInformation.class);
        return getKakaoTokenInformation.getId();
    }
}
