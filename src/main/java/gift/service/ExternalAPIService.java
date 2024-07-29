package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoTextMessageRequestDto;
import gift.entity.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.net.URI;

@Service
public class ExternalAPIService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalAPIService.class);
    final String kakaoOauthAuthorizeUrl = "https://kauth.kakao.com/oauth/authorize";
    final String kakaoOauthTokenUrl = "https://kauth.kakao.com/oauth/token";
    final String kakaoSendMessageToMeUrl = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private final RestTemplate client = new RestTemplateBuilder().build();

    Properties properties = new Properties();

    public String handleKakaoRedirect(String location) throws JsonProcessingException {

        URI uri = URI.create(location);
        var query = uri.getQuery();
        String[] params = query.split("&");

        String code = null;
        String state = null;

        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue[0].equals("code")) {
                code = keyValue[1];
            } else if (keyValue[0].equals("state")) {
                state = keyValue[1];
            }
        }

        if (code != null && state != null) {
            return getKakaoToken(code);
        }
        return null;
    }

    public String getKakaoToken(String code) throws JsonProcessingException {

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.getClientId());
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(kakaoOauthTokenUrl));
        var response = client.exchange(request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("Token response: " + response.getBody());
            String tokenResponse = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(tokenResponse);
            String accessToken = jsonNode.get("access_token").asText();
            return accessToken;

        } else {
            logger.error("토큰 가져오기 실패, 상태코드: " + response.getStatusCode());
            return null;
        }
    }


    public String getKakaoAuthorize() throws JsonProcessingException {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("response_type", "code");
        body.add("client_id", properties.getClientId());
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("state", "state");
        var request = new RequestEntity<>(body, headers, HttpMethod.GET, URI.create(kakaoOauthAuthorizeUrl));
        var response = client.exchange(request, String.class);

        if (response.getStatusCode() == HttpStatus.FOUND) {
            String location = response.getHeaders().getLocation().toString();
            return handleKakaoRedirect(location);
        }
        return null;
    }


    public ResponseEntity<Integer> sendKakaoMessageToMe(KakaoTextMessageRequestDto kakaoTextMessageRequestDto) throws JsonProcessingException {

        String token = getKakaoAuthorize();

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(token);

        var body = new LinkedMultiValueMap<String, Object>();
        body.add("template_object", kakaoTextMessageRequestDto);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(kakaoSendMessageToMeUrl));
        return client.exchange(request, Integer.class);
    }
}
