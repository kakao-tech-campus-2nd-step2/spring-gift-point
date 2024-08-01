package gift.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.common.auth.JwtUtil;
import gift.common.util.CommonResponse;
import gift.member.dto.LoginResponse;
import gift.member.dto.SignUpResponse;
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

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/members";
        memberRepository.deleteAll();
    }

    @Test
    public void testRegister() throws Exception {
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

        CommonResponse<SignUpResponse> commonResponse = objectMapper.readValue(response.getBody(), CommonResponse.class);
        SignUpResponse signUpResponse = objectMapper.convertValue(commonResponse.getData(), SignUpResponse.class);
        String token = signUpResponse.token().trim();

        assertThat(token).isNotNull();
        assertThat(jwtUtil.isTokenValid(token)).isTrue();
    }

    @Test
    public void testLogin() throws Exception {
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

        CommonResponse<SignUpResponse> commonRegisterResponse = objectMapper.readValue(registerResponse.getBody(), CommonResponse.class);
        SignUpResponse signUpResponse = objectMapper.convertValue(commonRegisterResponse.getData(), SignUpResponse.class);
        String registerToken = signUpResponse.token().trim();

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

        CommonResponse<LoginResponse> commonLoginResponse = objectMapper.readValue(loginResponse.getBody(), CommonResponse.class);
        LoginResponse loginResponseData = objectMapper.convertValue(commonLoginResponse.getData(), LoginResponse.class);
        String loginToken = loginResponseData.token().trim();

        assertThat(loginToken).isNotNull();
        assertThat(jwtUtil.isTokenValid(loginToken)).isTrue();
        assertThat(loginToken).isEqualTo(registerToken); // 로그인 토큰과 회원가입 토큰이 같아야 함
    }
}
