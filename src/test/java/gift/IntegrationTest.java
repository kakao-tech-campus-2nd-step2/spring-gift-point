package gift;

import gift.dto.user.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    private static final String REGISTER_URL = "/api/members/register";
    private static final String LOGIN_URL = "/api/members/login";
    private static final String ADD_GIFT_URL = "/wish/";
    @LocalServerPort
    private int port;
    private RestTemplate restTemplate;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void testAddGiftToCart() {
        String email = "abcd@naver.com";
        String password = "1234";

        // 회원가입
        registerUser(email, password);
        // 로그인
        String token = loginAndGetToken(email, password);
        // 위시리스트에 상품 추가
        addGiftToWish(token, 1, 2);
    }

    public void registerUser(String email, String password) {
        String registerUrl = baseUrl + REGISTER_URL;
        UserRequest.Create registerRequest = new UserRequest.Create(email, password);

        HttpEntity<UserRequest.Create> registerRequestEntity = new HttpEntity<>(registerRequest);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity(registerUrl, registerRequestEntity, String.class);

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(registerResponse.getBody()).isEqualTo("회원가입을 성공하였습니다!");
    }

    public String loginAndGetToken(String email, String password) {
        String loginUrl = baseUrl + LOGIN_URL;
        UserRequest.Create loginRequest = new UserRequest.Create(email, password);

        HttpEntity<UserRequest.Create> loginRequestEntity = new HttpEntity<>(loginRequest);
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(loginUrl, loginRequestEntity, Map.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).containsKeys("accessToken");

        String token = (String) loginResponse.getBody().get("accessToken");

        return token;
    }

    public void addGiftToWish(String token, int giftId, int quantity) {
        String addGiftUrl = baseUrl + ADD_GIFT_URL + giftId + "?quantity=" + quantity;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> addGiftRequestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> addGiftResponse = restTemplate.exchange(
                addGiftUrl,
                HttpMethod.POST,
                addGiftRequestEntity,
                String.class
        );

        assertThat(addGiftResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(addGiftResponse.getBody()).contains("위시리스트에 상품이 추가되었습니다.");
    }
}