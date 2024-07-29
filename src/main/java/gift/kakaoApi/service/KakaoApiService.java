package gift.kakaoApi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.order.entity.Orders;
import gift.kakaoApi.dto.message.Link;
import gift.kakaoApi.dto.message.TemplateObject;
import gift.kakaoApi.dto.token.KakaoTokenResponse;
import gift.kakaoApi.dto.userInfo.KakaoUserInfoResponse;
import gift.kakaoApi.exception.KakaoLoginException;
import gift.kakaoApi.properties.KakaoProperties;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoApiService {

    public static final String BEARER_PREFIX = "Bearer ";
    private final KakaoProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient client;

    public KakaoApiService(KakaoProperties properties, ObjectMapper objectMapper,
        RestClient restClient) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.client = restClient;
    }

    public String getKakaoLoginUri() {
        return UriComponentsBuilder.newInstance()
            .path(properties.authorizationCodeUrl())
            .queryParam("response_type", "code")
            .queryParam("client_id", properties.clientId())
            .queryParam("redirect_uri", properties.redirectUrl())
            .toUriString();
    }

    public KakaoTokenResponse getKakaoToken(String code) {
        LinkedMultiValueMap<String, String> body = creatBody(code);

        return client.post()
            .uri(URI.create(properties.tokenUrl()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .exchange((request, response) -> {
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new KakaoLoginException("카카오 인증 실패");
                }

                return objectMapper.readValue(response.getBody(), KakaoTokenResponse.class);
            });
    }

    public KakaoUserInfoResponse getKakaoAccount(String accessToken) {
        return client.get()
            .uri(URI.create(properties.accountUrl()))
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
            .retrieve()
            .body(KakaoUserInfoResponse.class);
    }

    public Boolean sendKakaoMessage(String accessToken, Orders order) {
        String orderMessage = createOrderMessage(order);
        Link link = new Link("http://localhost:8080", "http://localhost:8080");
        TemplateObject templateObject = new TemplateObject("text", orderMessage, link);

        return client.post()
            .uri(URI.create(properties.sendMessageUrl()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
            .body(templateObject.toRequestBody())
            .exchange((request, response) -> {
                if (response.getStatusCode().is2xxSuccessful()) {
                    return true;
                }
                return false;
            });
    }

    public String createOrderMessage(Orders order) {
        String messageTemplate = "[주문 내역]\n"
            + "\n[상품]\n"
            + "  상품명: %s\n"
            + "\n[옵션]\n"
            + "  옵션명: %s\n"
            + "  수량: %d\n"
            + "\n[요청사항]\n"
            + "  요청 메세지: %s\n"
            + "\n상품 주문이 완료되었습니다.\n";

        String productName = order.getOption().getProduct().getName();
        String optionName = order.getOption().getName();
        int quantity = order.getQuantity();
        String requestMessage = order.getMessage();

        return String.format(messageTemplate, productName, optionName, quantity, requestMessage);
    }

    private LinkedMultiValueMap<String, String> creatBody(String code) {

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
