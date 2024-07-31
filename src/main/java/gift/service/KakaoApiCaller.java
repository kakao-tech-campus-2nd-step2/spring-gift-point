package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.common.exception.AuthenticationException;
import gift.common.properties.KakaoProperties;
import gift.model.Orders;
import gift.service.dto.KakaoInfoDto;
import gift.service.dto.KakaoRequest;
import gift.service.dto.KakaoTokenDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.Duration;

@Component
public class KakaoApiCaller {

    private final KakaoProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient client;

    public KakaoApiCaller(ObjectMapper objectMapper, KakaoProperties properties) {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withReadTimeout(Duration.ofSeconds(2))
                .withConnectTimeout(Duration.ofSeconds(5));
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactories.get(settings);

        this.client = RestClient.builder().requestFactory(requestFactory).build();
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    public KakaoTokenDto getKakaoAccessToken(String code, String redirectUrl) {
        return client.post()
                .uri(URI.create(properties.tokenUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(createBodyForAccessToken(code, redirectUrl))
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                        return objectMapper.readValue(response.getBody(), KakaoTokenDto.class);
                    }
                    throw new AuthenticationException("Kakao login failed");
                });
    }

    public String getKakaoMemberInfo(String accessToken) {
        return client.post()
                .uri(URI.create(properties.memberInfoUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                        return objectMapper.readValue(response.getBody(), KakaoInfoDto.class).id() + "@Kakao";
                    }
                    throw new AuthenticationException("Kakao Info failed");
                });
    }

    public KakaoTokenDto refreshAccessToken(String refreshToken) {
        return client.post()
                .uri(URI.create(properties.refreshUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(createBodyForRefreshAccessToken(refreshToken))
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                        return objectMapper.readValue(response.getBody(), KakaoTokenDto.class);
                    }
                    throw new AuthenticationException("kakao token refresh failed");
                });
    }

    public void signOutKakao(String accessToken) {
        client.post()
                .uri(URI.create(properties.logoutUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new AuthenticationException("Logout failed");
                }).body(String.class);
    }

    public void sendKakaoMessage(String accessToken, Orders orders) {
        String rs = client.post()
                .uri(URI.create(properties.selfMessageUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .body(createBodyForMessage(orders))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    System.out.println("error");
                    throw new AuthenticationException("Message sending failed");
                }).body(String.class);
        System.out.println(rs);
    }


    private @NotNull LinkedMultiValueMap<String, String> createBodyForAccessToken(String code, String redirectUrl) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", redirectUrl);
        body.add("code", code);
        return body;
    }

    private @NotNull LinkedMultiValueMap<String, String> createBodyForRefreshAccessToken(String refreshToken) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "refreshToken");
        body.add("client_id", properties.clientId());
        body.add("refreshToken", refreshToken);
        return body;
    }

    private @NotNull LinkedMultiValueMap<String, Object> createBodyForMessage(Orders orders) {
        try {
            String template = objectMapper.writeValueAsString(KakaoRequest.Feed.from(orders));
            var body = new LinkedMultiValueMap<String, Object>();
            body.set("template_object", template);
            return body;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON Processing Error");
        }
    }
}
