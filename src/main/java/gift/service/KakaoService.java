package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.AuthToken;
import gift.domain.TokenInformation;
import gift.dto.response.OrderResponseDto;
import gift.exception.customException.JsonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static gift.utils.TokenConstant.REFRESH_TOKEN;

@Service
public class KakaoService {

    @Value("${kakao_api_scope}")
    private String SCOPE;

    @Value("${kakao_api_response_type}")
    private String RESPONSE_TYPE;

    @Value("${kakao_api_redirect_uri}")
    private String REDIRECT_URI;

    @Value("${kakao_api_client_id}")
    private String CLIENT_ID;

    @Value("${kakao_api_grant_type}")
    private String GRANT_TYPE;
    private final String ACCESS_TOKEN_API_URL = "https://kauth.kakao.com/oauth/token";
    private final String USER_INFORMATION_API_URL = "https://kapi.kakao.com/v2/user/me";
    private final String SEND_MESSAGE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public StringBuilder makeKakaOauthParameter() {
        StringBuilder builder = new StringBuilder();
        builder.append("scope=").append(SCOPE).append("&")
                .append("response_type=").append(RESPONSE_TYPE).append("&")
                .append("redirect_uri=").append(REDIRECT_URI).append("&")
                .append("client_id=").append(CLIENT_ID);

        return builder;
    }

    public TokenInformation getKakaoOauthToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        RequestEntity request = new RequestEntity<>(makeAccessTokenBody(code), headers, HttpMethod.POST, URI.create(ACCESS_TOKEN_API_URL));
        ResponseEntity<String> response = restTemplate.exchange(ACCESS_TOKEN_API_URL, HttpMethod.POST, request, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return new TokenInformation(jsonNode);
        } catch (JsonProcessingException e) {
            throw new JsonException();
        } catch (Exception e) {
            throw new RuntimeException("예상하지 못한 EXCEPTION 발생");
        }
    }

    public String getKakaoUserInformation(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(accessToken);

        RequestEntity request = new RequestEntity<>(headers, HttpMethod.POST, URI.create(USER_INFORMATION_API_URL));
        ResponseEntity<String> response = restTemplate.exchange(USER_INFORMATION_API_URL, HttpMethod.POST, request, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("id").asText();
        } catch (JsonProcessingException e) {
            throw new JsonException();
        } catch (Exception e) {
            throw new RuntimeException("예상하지 못한 EXCEPTION 발생");
        }
    }

    public TokenInformation renewToken(AuthToken authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        RequestEntity request = new RequestEntity<>(makeTokenRenewBody(authToken.getRefreshToken()), headers, HttpMethod.POST, URI.create(ACCESS_TOKEN_API_URL));
        ResponseEntity<String> response = restTemplate.exchange(ACCESS_TOKEN_API_URL, HttpMethod.POST, request, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return new TokenInformation(jsonNode);
        } catch (JsonProcessingException e) {
            throw new JsonException();
        } catch (Exception e) {
            throw new RuntimeException("예상하지 못한 EXCEPTION 발생");
        }
    }

    public void sendKakaoMessage(String accessToken, OrderResponseDto orderResponseDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("template_object", makeMessageBody(orderResponseDto));

        RequestEntity request = new RequestEntity<>(parameters, headers, HttpMethod.POST, URI.create(SEND_MESSAGE_URL));
        restTemplate.exchange(SEND_MESSAGE_URL, HttpMethod.POST, request, String.class);
    }

    private LinkedMultiValueMap<String, String> makeAccessTokenBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);
        return body;
    }

    private LinkedMultiValueMap<String, String> makeTokenRenewBody(String refreshToken) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", REFRESH_TOKEN);
        body.add("client_id", CLIENT_ID);
        body.add("refresh_token", refreshToken);
        return body;
    }

    private String makeMessageBody(OrderResponseDto orderResponseDto) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\"object_type\":\"text\",\"text\":\"")
                .append(orderResponseDto.id())
                .append(" 번 옵션이 주문 되었습니다. ")
                .append(orderResponseDto.message())
                .append("\",\"link\":{\"web_url\":\"\"}}");

        return sb.toString();
    }

}
