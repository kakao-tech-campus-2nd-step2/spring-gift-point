package gift.service;//package gift.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import gift.common.exception.AuthenticationException;
//import gift.common.properties.KakaoProperties;
//import gift.model.Orders;
//import gift.service.dto.KakaoRequest;
//import gift.service.dto.KakaoTokenDto;
//import jakarta.validation.constraints.NotNull;
//import org.junit.jupiter.api.Test;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestClient;
//
//import java.net.URI;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ActiveProfiles("test")
//@SpringBootTest
//public class KakaoApiCallerTest {
//    private final RestClient client = RestClient.builder().build();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Autowired
//    private KakaoProperties properties;
//
//    @MockBean
//    private RedissonClient redissonClient;
//
//    @Test
//    void test0() {
//        System.out.println(properties);
//    }
//
//    @Test
//    void getKakaoAccessToken() {
//        var url = properties.tokenUrl();
//        var code = "rm8IdrNXuL_DW2gUvEpqax3f4oQzJyDInTnyqlTXqUG0jcjIAzdORAAAAAQKKclgAAABkOqP91Sxu3fh8M0xkQ";
//        final var body = createBody(code);
//        var response = client.post()
//                .uri(URI.create(url))
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(body)     // request body
//                .exchange((req, res) -> {
//                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
//                    if (res.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
//                        return objectMapper.readValue(res.getBody(), KakaoTokenDto.class);
//                    }
//                    return "";
//                });
//        System.out.println(response);
//    }
//
//    @Test
//    void getKakaoMemberInfo() {
//        var accessToken = "EXzy9fHlc7wjc6GlfR1uRVo9yKYPTqJHAAAAAQorDR8AAAGQ3w6vZyn2EFsnJsRZ?";
//        var response = client.post()
//                .uri(URI.create(properties.memberInfoUrl()))
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .header("Authorization", "Bearer " + accessToken)
//                .retrieve()
//                .toEntity(String.class);
//
//        System.out.println(response);
//    }
//
//    @Test
//    void refreshAccessToken() {
//        var refreshToken = "AjDT-6Ig_9NF361BAz4n2CFJTO-h7OO4AAAAAgo9cxgAAAGQ5PWPECn2EFsnJsRZ";
//        var response = client.post()
//                .uri(URI.create(properties.refreshUrl()))
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(createBodyForRefreshAccessToken(refreshToken))
//                .exchange((req, res) -> {
//                    if (res.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
//                        return objectMapper.readValue(res.getBody(), KakaoTokenDto.class);
//                    }
//                    throw new AuthenticationException("kakao token refresh failed");
//                });
//
//        System.out.println(response);
//    }
//
//    @Test
//    void signOutKakao() {
//        String accessToken = "kqVhTXL0jC2IZaWxvlqYCEXAFososlVYAAAAAQo9c00AAAGQ5PdLGCn2EFsnJsRZ";
//        var res = client.post()
//                .uri(URI.create(properties.logoutUrl()))
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .header("Authorization", "Bearer " + accessToken)
//                .retrieve()
//                .onStatus(HttpStatusCode::isError, (request, response) -> {
//                    throw new AuthenticationException("Logout failed");
//                });
//        System.out.println(res);
//    }
//
//    @Test
//    void sendSelfMessage() throws JsonProcessingException {
//        String accessToken = "9oPh4kFJB_mKt-94b_3hXPZ9HEwB1UiDAAAAAQo8JCAAAAGQ6pC9Eyn2EFsnJsRZ";
//        Orders orders = new Orders(1L, 2L, 3L, "상품명", "옵션명",
//                1000, 10, "이건 설명이다.");
//        KakaoRequest.Feed feed = KakaoRequest.Feed.from(orders);
//
//        String template_str = objectMapper.writeValueAsString(feed);
//        MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
//        map.set("template_object", template_str);
//
//        var res = client.post()
//                .uri(URI.create(properties.selfMessageUrl()))
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .header("Authorization", "Bearer " + accessToken)
//                .body(map)
//                .retrieve()
//                .toEntity(String.class);
//
//        System.out.println(res);
//    }
//
//    private @NotNull LinkedMultiValueMap<String, String> createBody(String code) {
//        var body = new LinkedMultiValueMap<String, String>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", properties.clientId());
//        body.add("redirect_uri", properties.redirectUrl());
//        body.add("code", code);
//        return body;
//    }
//
//    private @NotNull LinkedMultiValueMap<String, String> createBodyForRefreshAccessToken(String refreshToken) {
//        var body = new LinkedMultiValueMap<String, String>();
//        body.add("grant_type", "refreshToken");
//        body.add("client_id", properties.clientId());
//        body.add("refreshToken", refreshToken);
//        return body;
//    }
//}