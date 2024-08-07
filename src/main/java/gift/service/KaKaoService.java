package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.KakaoProperties;
import gift.dto.KakaoTokenInfo;
import gift.dto.KakaoUserInfo;
import gift.dto.TemplateObject;
import gift.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static gift.exception.ErrorCode.KAKAO_LOGIN_FAILED_ERROR;
import static gift.exception.ErrorCode.SEND_MSG_FAILED_ERROR;

@Service
public class KaKaoService {

    private final KakaoProperties properties;
    private final KakaoProperties.Url url;

    @Value("${service.home.web_url}")
    private String homeUrl;

    private final RestClient client = RestClient.create();

    public KaKaoService(KakaoProperties properties) {
        this.properties = properties;
        url = properties.getUrl();
    }

    public String getKakaoAccountEmail(String accessToken) {
        ObjectMapper objectMapper = new ObjectMapper();

        ResponseEntity<String> response = clientWithAuthHeader(accessToken)
                .get()
                .uri(URI.create(url.getUserInfo()))
                .retrieve()
                .toEntity(String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(KAKAO_LOGIN_FAILED_ERROR);
        }

        try {
            return objectMapper.readValue(response.getBody(), KakaoUserInfo.class).kakaoAccount().email;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public KakaoTokenInfo getKakaoTokenInfo(String code) {
        ObjectMapper objectMapper = new ObjectMapper();

        LinkedMultiValueMap<String, String> body = createGetTokenBody(code);

        ResponseEntity<String> response = client.post()
                .uri(URI.create(url.getToken()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(KAKAO_LOGIN_FAILED_ERROR);
        }

        try {
            return objectMapper.readValue(response.getBody(), KakaoTokenInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private LinkedMultiValueMap<String, String> createGetTokenBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", properties.getGrantType());
        body.add("client_id", properties.getClientId());
        body.add("redirect_url", properties.getClientId());
        body.add("code", code);
        return body;
    }


    public ResponseEntity<String> sendMessage(String message, String accessToken) {
        LinkedMultiValueMap<String, String> body = createSendMsgBody(message);

        ResponseEntity<String> response = clientWithAuthHeader(accessToken)
                .post()
                .uri(URI.create(url.getSendMessage()))
                .body(body)
                .retrieve()
                .toEntity(String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(SEND_MSG_FAILED_ERROR);
        }

        return response;
    }

    private LinkedMultiValueMap<String, String> createSendMsgBody(String message) {
        LinkedMultiValueMap<String, String> templateObject = new LinkedMultiValueMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        TemplateObject template = new TemplateObject("text", message, new TemplateObject.Link(homeUrl));

        try {
            String jsonTemplateObject = objectMapper.writeValueAsString(template);
            templateObject.add("template_object", jsonTemplateObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return templateObject;
    }

    private RestClient clientWithAuthHeader(String accessToken) {
        return RestClient.builder().defaultHeader("Authorization", "Bearer " + accessToken).build();
    }

}
