package gift.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.user.exception.OauthCustomException.FailedToSendMessageException;
import gift.user.model.dto.AppUser;
import gift.user.model.dto.KakaoMessageTemplate;
import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestClient client = RestClient.builder().build();

    public void sendMessageToMe(AppUser user, String message) {
        try {
            String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
            String header = "Bearer " + user.getAccessToken();
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

            KakaoMessageTemplate template = new KakaoMessageTemplate(message);
            try {
                String jsonTemplate = objectMapper.writeValueAsString(template);
                multiValueMap.add("template_object", jsonTemplate);
            } catch (JsonProcessingException e) {
                throw new FailedToSendMessageException();
            }

            client.post()
                    .uri(URI.create(url))
                    .body(multiValueMap)
                    .header("Authorization", header)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            throw new FailedToSendMessageException();
        }
    }

    private boolean sendRequest(String url, String header, MultiValueMap<String, String> values) {
        try {
            client.post()
                    .uri(URI.create(url))
                    .body(values)
                    .header("Authorization", header)
                    .retrieve()
                    .body(String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
}
