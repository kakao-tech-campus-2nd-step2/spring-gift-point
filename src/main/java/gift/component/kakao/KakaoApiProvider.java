package gift.component.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gift.auth.OAuthToken;
import gift.dto.KakaoMessageRequestDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class KakaoApiProvider {

    private final KakaoProperties kakaoProperties;
    private final ObjectMapper objectMapper;

    private static final String GRANT_TYPE = "authorization_code";
    public static final String KAKAO_TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";
    public static final String KAKAO_USER_PROFILE_URI = "https://kapi.kakao.com/v2/user/me";
    public static final String KAKAO_MESSAGE_API_URI = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    public static final String KAKAO_EMAIL = "kakao@kakao_";
    public static final String KAKAO_PASSWORD = "KAKAO";
    private static final String WEB_URL = "http://www.daum.net";
    private static final String BUTTON_TITLE = "바로가기";

    public KakaoApiProvider(KakaoProperties kakaoProperties, ObjectMapper objectMapper) {
        this.kakaoProperties = kakaoProperties;
        this.objectMapper = objectMapper;
    }

    public OAuthToken parseOAuthToken(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, OAuthToken.class);
    }

    public HttpHeaders makeHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    public MultiValueMap<String, String> makeGetAccessTokenBody(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", kakaoProperties.kakaoClientId());
        body.add("redirect_uri", kakaoProperties.kakaoRedirectUrl());
        body.add("code", code);
        return body;
    }

    public MultiValueMap<String, String> makeTemplateObject(KakaoMessageRequestDto kakaoMessageRequestDto) {
        String text = generateOrderText(kakaoMessageRequestDto);

        ObjectNode templateJson = objectMapper.createObjectNode();
        templateJson.put("object_type", "text");
        templateJson.put("text", text);
        templateJson.set("link", createLinkNode());
        templateJson.put("button_title", BUTTON_TITLE);

        String templateObject = templateJson.toString();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObject);

        return body;
    }

    private String generateOrderText(KakaoMessageRequestDto kakaoMessageRequestDto) {
        String productName = kakaoMessageRequestDto.productName();
        String optionName = kakaoMessageRequestDto.optionName();
        int num = kakaoMessageRequestDto.quantity();

        return productName + "[" + optionName + "]" + " 상품이 " + num + "개 주문되었습니다.";
    }

    private ObjectNode createLinkNode() {
        ObjectNode link = objectMapper.createObjectNode();
        link.put("web_url", WEB_URL);

        return link;
    }

}
