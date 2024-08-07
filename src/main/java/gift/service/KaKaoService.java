package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Value("${kakao.clientId}")
    private String clientId;

    @Value("${kakao.redirectUrl}")
    private String redirectUrl;

    @Value("${kakao.grant-type}")
    private String grantType;

    @Value("${kakao.get-token.url}")
    private String getTokenUrl;

    @Value("${kakao.get-uerInfo.url}")
    private String getUserInfoUrl;

    @Value("${kakao.send-message.url}")
    private String sendMessageUrl;

    @Value("${service.home.web_url}")
    private String homeUrl;

    private final RestClient client = RestClient.create();

    public String getKakaoAccountEmail(String accessToken) {
        ObjectMapper objectMapper = new ObjectMapper();

        ResponseEntity<String> response = clientWithAuthHeader(accessToken)
                .get()
                .uri(URI.create(getUserInfoUrl))
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
                .uri(URI.create(getTokenUrl))
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
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("redirect_url", redirectUrl);
        body.add("code", code);
        return body;
    }


    public ResponseEntity<String> sendMessage(String message, String accessToken) {
        LinkedMultiValueMap<String, String> body = createSendMsgBody(message);

        ResponseEntity<String> response = clientWithAuthHeader(accessToken)
                .post()
                .uri(URI.create(sendMessageUrl))
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

    public void setSendMessageUrl(String sendMessageUrl) {
        this.sendMessageUrl = sendMessageUrl;
    }

    public void setGetTokenUrl(String getTokenUrl) {
        this.getTokenUrl = getTokenUrl;
    }

    public void setGetUserInfoUrl(String getUserInfoUrl) {
        this.getUserInfoUrl = getUserInfoUrl;
    }

}
