package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.model.kakao.KakaoAuth;
import gift.model.kakao.KakaoMember;
import gift.model.kakao.LinkObject;
import gift.model.kakao.TemplateObject;
import gift.service.intercptor.ClientInterceptor;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class KakaoRestClient implements KakaoApi{
    private static final Logger logger = LoggerFactory.getLogger(KakaoRestClient.class);

    private final RestClient client;
    private final KakaoProperties kakaoProperties;

    public KakaoRestClient(RestClient.Builder builder,  KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(kakaoProperties.getConnectTimeout());
        requestFactory.setReadTimeout(kakaoProperties.getReadTimeout());

        this.client = builder
                .requestFactory(requestFactory)
                .requestInterceptor(new ClientInterceptor())
                .build();
    }

    @Override
    public KakaoAuth getKakaoToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = createBody(code);
        var response =  this.client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(KakaoAuth.class);
        return response.getBody();
    }

    @Override
    public KakaoMember getKakaoMemberId(String token) {
        var url = "https://kapi.kakao.com/v2/user/me";
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var response = this.client.get()
                .uri(url)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headers);
                })
                .retrieve()
                .toEntity(KakaoMember.class);

        return response.getBody();
    }

    @Override
    public void sendKakaoMessage(String token, String message) throws JsonProcessingException {
        logger.info("sendKakaoMessage");
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var body = createTemplateObject(message);

        var response = this.client.post()
                .uri(url)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headers);
                })
                .body(body)
                .retrieve()
                .toEntity(String.class);

        logger.info("sendKakaoMessget result" + response);
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody(String code){
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUrl());
        body.add("code", code);
        return body;
    }

    private @NotNull LinkedMultiValueMap<String, String> createTemplateObject(String message) throws JsonProcessingException {
        String objectType = "text";
        String webUrl = "http://localhost:8080/";
        String buttonTitle = "바로가기";

        LinkObject link = new LinkObject(webUrl);
        TemplateObject templateObject = new TemplateObject(objectType, message, link, buttonTitle);

        String jsonString = new ObjectMapper().writeValueAsString(templateObject);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", jsonString);

        return body;
    }
}
