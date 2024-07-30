package gift.util;

import static gift.util.url.KakaoUrl.redirectUrl;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import gift.auth.domain.KakaoToken.kakaoInfo;
import gift.auth.domain.KakaoToken.kakaoToken;
import gift.domain.SendKakao.Link;
import gift.domain.SendKakao.Message;
import gift.util.url.KakaoUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class ApiCall {

    //    private final RestTemplate restTemplate = new RestTemplate();
    private final RestCall restCall;
    private final String clientId;
    private final String commonUrl = "http://localhost:8080";

    @ConfigurationProperties("kakao")
    public record kakaoProperties(String clientId) {

    }

    @Autowired
    public ApiCall(kakaoProperties properties, RestCall restCall) {
        this.clientId = properties.clientId();
        this.restCall = restCall;
    }

    public String getKakaoCode() {
        String url = KakaoUrl.getCode
            + "?response_type=code"
            + "&redirect_uri=" + commonUrl + redirectUrl
            + "&through_account=true"
            + "&client_id=" + clientId;

        return url;
    }

    public kakaoToken getKakaoToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);

        String url = KakaoUrl.getToken + "?grant_type=authorization_code"
            + "&client_id=" + clientId
            + "&redirect_uri=" + commonUrl + redirectUrl
            + "&code=" + code;

        ResponseEntity<kakaoToken> responseEntity = restCall.apiCall(url, GET, requestEntity,
            kakaoToken.class);
        System.out.println(responseEntity.getBody());
        return responseEntity.getBody();
    }

    public kakaoInfo getKakaoTokenInfo(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);

        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);

        String url = KakaoUrl.getInfo;

        ResponseEntity<kakaoInfo> responseEntity = restCall.apiCall(url, GET, requestEntity,
            kakaoInfo.class);
        return responseEntity.getBody();
    }

    public void sendMessageForMe(String token, String text, String webUrl, String mobileUrl) {
        Message message = new Message(text, new Link(webUrl, mobileUrl));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("template_object", message.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);

        var requestEntity = new HttpEntity(body, headers);

        String url = KakaoUrl.sendMessage;

        restCall.apiCall(url, POST, requestEntity, String.class);
    }
}
