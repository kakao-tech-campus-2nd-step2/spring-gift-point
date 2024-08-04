package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.common.exception.AuthenticationException;
import gift.common.properties.KakaoProperties;
import gift.service.dto.KakaoInfoDto;
import gift.service.dto.KakaoRequest;
import gift.service.dto.KakaoTokenDto;
import gift.service.dto.OrderDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class KakaoApiCaller {

    private final KakaoProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient client;

    public KakaoApiCaller(RestClient client, ObjectMapper objectMapper, KakaoProperties properties) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    public KakaoTokenDto getKakaoAccessToken(String code, String redirectUrl) {
        try {
            return client.post()
                    .uri(URI.create(properties.tokenUrl()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(createBodyForAccessToken(code, redirectUrl))
                    .exchange((request, response) -> {
                        if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                            return objectMapper.readValue(response.getBody(), KakaoTokenDto.class);
                        }
                        throw new AuthenticationException("유효하지 않은 인가코드입니다.");
                    });
        } catch (ResourceAccessException e) {
            throw new AuthenticationException("네트워크 환경이 불안정합니다.");
        }
    }

    public KakaoInfoDto getKakaoMemberInfo(String accessToken) {
        try {
            return client.post()
                    .uri(URI.create(properties.memberInfoUrl()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("Authorization", "Bearer " + accessToken)
                    .exchange((request, response) -> {
                        if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                            KakaoInfoDto.Info info = objectMapper.readValue(response.getBody(), KakaoInfoDto.Info.class);
                            return new KakaoInfoDto(info.id() + "@Kakao", info.properties().nickname());
                        }
                        throw new AuthenticationException("로그인이 만료되었습니다.");
                    });
        } catch (ResourceAccessException e) {
            throw new AuthenticationException("네트워크 환경이 불안정합니다.");
        }
    }

    public KakaoTokenDto refreshAccessToken(String refreshToken) {
        try {
            return client.post()
                    .uri(URI.create(properties.refreshUrl()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(createBodyForRefreshAccessToken(refreshToken))
                    .exchange((request, response) -> {
                        if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                            return objectMapper.readValue(response.getBody(), KakaoTokenDto.class);
                        }
                        throw new AuthenticationException("토큰 갱신에 실패하였습니다.");
                    });
        } catch (ResourceAccessException e) {
            throw new AuthenticationException("네트워크 환경이 불안정합니다.");
        }
    }

    public void signOutKakao(String accessToken) {
        try {
            client.post()
                    .uri(URI.create(properties.logoutUrl()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        throw new AuthenticationException("로그아웃 실패");
                    }).body(String.class);
        } catch (ResourceAccessException e) {
            throw new AuthenticationException("네트워크 환경이 불안정합니다.");
        }
    }

    public void sendKakaoMessage(String accessToken, OrderDto orders) {
        try {
            client.post()
                    .uri(URI.create(properties.selfMessageUrl()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(createBodyForMessage(orders))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        throw new AuthenticationException("메세지 전송 실패");
                    }).toBodilessEntity();
        } catch (ResourceAccessException e) {
            throw new AuthenticationException("네트워크 환경이 불안정합니다.");
        }
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

    private @NotNull LinkedMultiValueMap<String, Object> createBodyForMessage(OrderDto orders) {
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
