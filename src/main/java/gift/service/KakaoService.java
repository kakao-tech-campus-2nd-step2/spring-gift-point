package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
public class KakaoService {

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String MESSAGE_SEND_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    private final KakaoTokenService kakaoTokenService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public KakaoService(KakaoTokenService kakaoTokenService, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.kakaoTokenService = kakaoTokenService;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public String getClientId() {
        return client_id;
    }

    public String getRedirectUri() {
        return redirect_uri;
    }

    public String getToken(String code) {
        // HTTP 헤더 설정
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        // HTTP 요청 바디 설정
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);

        // 요청 엔티티 생성
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(TOKEN_URL));

        // RestTemplate을 사용하여 POST 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // JSON 응답을 파싱하여 액세스 토큰 추출
        try {
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
            return (String) responseBody.get("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(USER_INFO_URL));

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        try {
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
            Long id = (Long) responseBody.get("id");
            return id.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public void sendToMe(String accessToken, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        JSONObject linkObj = new JSONObject();
        linkObj.put("web_url", "https://developers.kakao.com/");
        linkObj.put("mobile_web_url", "https://developers.kakao.com/");

        JSONObject templateObj = new JSONObject();
        templateObj.put("object_type", "text");
        templateObj.put("text", message);
        templateObj.put("link", linkObj);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObj.toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        restTemplate.exchange(MESSAGE_SEND_URL, HttpMethod.POST, request, String.class);
    }
}
