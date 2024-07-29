package gift.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.auth.KakaoMemberInfoResponse;
import gift.controller.auth.KakaoTokenResponse;
import gift.controller.order.OrderRequest;
import gift.exception.KakaoMessageException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

public class KakaoUtil {

    private static final String KAKAO_API_URL = "https://developers.kakao.com";
    private static final String SEND_MSG_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private static final String BUTTON_TITLE = "선물 확인하기";

    public static void SendKakaoMessageToMe(
        OrderRequest orderRequest, KakaoTokenResponse kakaoToken) {
        HttpHeaders headers = createAuthorizationHeaders(kakaoToken);
        MultiValueMap<String, String> body = createTemplateObject(orderRequest);
        new RestTemplate().exchange(SEND_MSG_URL, HttpMethod.POST,
            new HttpEntity<>(body, headers), String.class);
    }

    private static HttpHeaders createAuthorizationHeaders(KakaoTokenResponse kakaoToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoToken.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private static MultiValueMap<String, String> createTemplateObject(OrderRequest orderRequest) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", orderRequest.message());
        Map<String, String> link = new HashMap<>();
        link.put("web_url", KAKAO_API_URL);
        link.put("mobile_web_url", KAKAO_API_URL);
        templateObject.put("link", link);
        templateObject.put("button_title", BUTTON_TITLE);
        String templateObjectString = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            templateObjectString = objectMapper.writeValueAsString(templateObject);
        } catch (Exception e) {
            throw new KakaoMessageException("failed creating kakao message");
        }
        body.add("template_object", templateObjectString);
        return body;
    }
}
