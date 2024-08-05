package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.repository.OAuth2AccessTokenRepository;
import gift.request.kakaomessage.KakaoLink;
import gift.response.KakaoMessageToMeResponse;
import gift.response.order.OrderResponse;
import gift.service.KakaoMessageService;
import gift.service.OrderService;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureMockMvc
class OrderApiControllerTest {

    @Autowired
    private WebClient client;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;
    @MockBean
    private KakaoMessageService kakaoMessageService;
    @MockBean
    private OAuth2AccessTokenRepository oAuth2AccessTokenRepository;

    private MockWebServer mockWebServer;
    private String mockWebServerUri;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();


    }

    @AfterEach
    void shutDown() throws IOException {
        mockWebServer.shutdown();
    }


    @DisplayName("나에게 보내기 테스트")
    @Test
    void sendToMe() throws JsonProcessingException {
        //given
        OrderResponse orderResponse = new OrderResponse(1L, 1L, 5,
            "2024:07:26 00:00:00", "주문이 완료되었습니다.");
        KakaoMessageToMeResponse messageToMeResponse = new KakaoMessageToMeResponse(0);
        KakaoLink link = KakaoLink.createLink();
        String accessToken = "accessToken";
        LinkedMultiValueMap<String, String> body = demoBody(orderResponse.message(), link);

        String messageUri = "/v2/api/talk/memo/default/send";
        mockWebServerUri = mockWebServer.url(messageUri).toString();
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setBody(objectMapper.writeValueAsString(messageToMeResponse))
        );

        given(kakaoMessageService.createTextMessage(any(String.class), any(KakaoLink.class)))
            .willReturn(body);
        //when

        ResponseEntity<String> response = client.post()
            .uri(URI.create(mockWebServerUri))
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(Mono.just(kakaoMessageService.createTextMessage(orderResponse.message(), link)),
                LinkedMultiValueMap.class)
            .retrieve()
            .toEntity(String.class)
            .timeout(Duration.ofMillis(500))
            .retry(3)
            .block();
        //then
        KakaoMessageToMeResponse toMeResponse = objectMapper.readValue(response.getBody(),
            KakaoMessageToMeResponse.class);
        assertThat(toMeResponse).isNotNull();
        assertThat(toMeResponse.resultCode()).isEqualTo(0);

    }

    @DisplayName("나에게 보내기 실패 테스트 - Bad Request")
    @Test
    void failSendToMe() throws JsonProcessingException {
        //given
        OrderResponse orderResponse = new OrderResponse(1L, 1L, 5,
            "2024:07:26 00:00:00", "주문이 완료되었습니다.");
        KakaoLink link = KakaoLink.createLink();
        String accessToken = "invalid_accessToken";
        LinkedMultiValueMap<String, String> body = demoBody(orderResponse.message(), link);

        String messageUri = "/v2/api/talk/memo/default/send";
        mockWebServerUri = mockWebServer.url(messageUri).toString();
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.BAD_REQUEST.value())
            .setBody("error: 400 Bad Request from POST /v2/api/talk/memo/default/send")
        );

        given(kakaoMessageService.createTextMessage(any(String.class), any(KakaoLink.class)))
            .willReturn(body);
        //when //then
        assertThatThrownBy( () -> client.post()
            .uri(URI.create(mockWebServerUri))
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(Mono.just(kakaoMessageService.createTextMessage(orderResponse.message(), link)),
                LinkedMultiValueMap.class)
            .retrieve()
            .toEntity(String.class)
            .block()).isInstanceOf(WebClientResponseException.class);
    }








    private LinkedMultiValueMap<String,String> demoBody(String message, KakaoLink link)
        throws JsonProcessingException {
        LinkedMultiValueMap<String,String> body = new LinkedMultiValueMap<>();

        HashMap<String,Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", message);
        templateObject.put("link",  link);

        body.add("template_object", objectMapper.writeValueAsString(templateObject));
        return body;
    }
}