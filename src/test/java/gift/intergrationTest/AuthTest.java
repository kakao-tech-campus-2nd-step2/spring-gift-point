package gift.intergrationTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.exception.ErrorCode;
import gift.model.user.User;
import gift.repository.UserRepository;
import gift.service.JwtProvider;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;

    private final Long testId = 1L;
    private final String testEmail = "test@test";
    private final String testPassword = "testPw";
    private final User testUser = new User(testId, testEmail, testPassword);
    private final String requestUrlPrefix = "http://localhost:";

    @BeforeEach
    void initDataBase() {
        userRepository.deleteAll();
    }

    String initForToken() {
        restTemplate.postForObject(requestUrlPrefix + port + "/register", testUser, Long.class);
        ResponseEntity<String> result = restTemplate.postForEntity(
            requestUrlPrefix + port + "/login", testUser, String.class);
        return result.getBody();
    }

    @Test
    @DisplayName("권한이 필요한 요청 성공")
    void testAuthRequestSuccess() {
        String token = initForToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        RequestEntity<?> request = new RequestEntity<>(headers, HttpMethod.GET,
            URI.create(requestUrlPrefix + port + "/wishes"));
        ResponseEntity<String> result = restTemplate.exchange(request, String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    @DisplayName("권한이 필요한 요청 실패(토큰 없이 접근 시)")
    void testAuthRequestWithoutTokenCase() {
        HttpHeaders headers = new HttpHeaders();
        RequestEntity<?> request = new RequestEntity<>(headers, HttpMethod.GET,
            URI.create(requestUrlPrefix + port + "/wishes"));
        ResponseEntity<String> result = restTemplate.exchange(request, String.class);
        assertAll(
            ()->assertThat(result.getStatusCode()).isEqualTo(ErrorCode.INVALID_TOKEN.getStatus()),
            ()->assertThat(result.getBody()).contains(ErrorCode.INVALID_TOKEN.getMessage())
        );
    }
    @Test
    @DisplayName("권한이 필요한 요청 실패(인가되지 않은 토큰)")
    void testAuthRequestWithInvalidTokenCase() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("testToken");
        RequestEntity<?> request = new RequestEntity<>(headers, HttpMethod.GET,
            URI.create(requestUrlPrefix + port + "/wishes"));
        ResponseEntity<String> result = restTemplate.exchange(request, String.class);
        assertAll(
            ()->assertThat(result.getStatusCode()).isEqualTo(ErrorCode.INVALID_TOKEN.getStatus()),
            ()->assertThat(result.getBody()).contains(ErrorCode.INVALID_TOKEN.getMessage())
        );
    }
}
