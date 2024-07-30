package gift.login;

import gift.common.auth.JwtUtil;
import gift.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// E2E 테스트
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberRegisterLoginTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/members";
        memberRepository.deleteAll();
    }

    @Test
    public void testRegister() {
        // given
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        request.put("password", "password");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/register",
                HttpMethod.POST,
                entity,
                String.class
        );

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        String token = response.getBody();
        assertThat(token).isNotNull();
        assertThat(jwtUtil.isTokenValid(token)).isTrue();
    }

    @Test
    public void testLogin() {
        // given
        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("email", "test@example.com");
        registerRequest.put("password", "password");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Map<String, String>> registerEntity = new HttpEntity<>(registerRequest, headers);

        // 회원가입하기
        ResponseEntity<String> registerResponse = restTemplate.exchange(
                baseUrl + "/register",
                HttpMethod.POST,
                registerEntity,
                String.class
        );
        assertThat(registerResponse.getStatusCodeValue()).isEqualTo(200);
        String registerToken = registerResponse.getBody();
        assertThat(registerToken).isNotNull();
        assertThat(jwtUtil.isTokenValid(registerToken)).isTrue();

        // when
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "test@example.com");
        loginRequest.put("password", "password");

        HttpEntity<Map<String, String>> loginEntity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> loginResponse = restTemplate.exchange(
                baseUrl + "/login",
                HttpMethod.POST,
                loginEntity,
                String.class
        );

        // then
        assertThat(loginResponse.getStatusCodeValue()).isEqualTo(200);
        String loginToken = loginResponse.getBody();
        assertThat(loginToken).isNotNull();
        assertThat(jwtUtil.isTokenValid(loginToken)).isTrue();
        assertThat(loginToken).isEqualTo(registerToken); // 로그인 토큰과 회원가입 토큰이 같아야 함
    }
}
