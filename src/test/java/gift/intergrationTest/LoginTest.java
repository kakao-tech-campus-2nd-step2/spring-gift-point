package gift.intergrationTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.exception.ErrorCode;
import gift.exception.ErrorResponseDTO;
import gift.model.user.User;
import gift.repository.UserRepository;
import gift.service.JwtProvider;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private
    JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;

    private final Long testId = 1L;
    private final String testEmail = "test@test";
    private final String testPassword = "testPw";
    private final User testUser = new User(testId, testEmail, testPassword);

    @BeforeEach
    void initDataBase() {
        userRepository.deleteAll();
    }

    void initForLogin() {
        restTemplate.postForObject("http://localhost:" + port + "/register", testUser, Long.class);
    }

    @Test
    @DisplayName("회원 가입 성공")
    void testRegisterSuccess() {
        var url = "http://localhost:" + port + "/register";
        ResponseEntity<Long> result = restTemplate.postForEntity(url, testUser, Long.class);
        assertAll(
            () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(result.getBody()).isEqualTo(
                userRepository.findByEmail(testEmail).get().getId())
        );
    }

    @Test
    @DisplayName("회원 가입 실패(이미 존재하는 이메일)")
    void testRegisterFailDuplicateEmail() {
        var url = "http://localhost:" + port + "/register";
        Map<String, String> error = new HashMap<>();
        error.put("email", ErrorCode.DUPLICATE_EMAIL.getMessage());
        initForLogin();

        ErrorResponseDTO expected = new ErrorResponseDTO(ErrorCode.INVALID_INPUT, error);

        ResponseEntity<ErrorResponseDTO> result = restTemplate.postForEntity(url, testUser,
            ErrorResponseDTO.class);
        assertThat(result.getBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("로그인 성공")
    void testLoginSuccess() {
        var url = "http://localhost:" + port + "/login";
        initForLogin();

        ResponseEntity<String> result = restTemplate.postForEntity(url, testUser, String.class);
        assertAll(
            () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(jwtProvider.validateToken(result.getBody())).isTrue()
        );
    }

    @Test
    @DisplayName("로그인 실패(존재하지 않는 이메일)")
    void testLoginFailCaseUnExistsEmail() {
        var url = "http://localhost:" + port + "/login";
        User unregistered = new User(0L, "1234@1234", testPassword);
        Map<String, String> error = new HashMap<>();
        error.put("email", ErrorCode.EMAIL_NOT_FOUND.getMessage());
        ErrorResponseDTO expected = new ErrorResponseDTO(ErrorCode.EMAIL_NOT_FOUND, error);
        initForLogin();
        ResponseEntity<ErrorResponseDTO> result = restTemplate.postForEntity(url, unregistered,
            ErrorResponseDTO.class);

        assertThat(result.getBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("로그인 실패(비밀번호 불일치)")
    void testLoginFailCasePassWordMisMatch() {
        var url = "http://localhost:" + port + "/login";
        User unregistered = new User(0L, testEmail, "wrongPw");
        Map<String, String> error = new HashMap<>();
        error.put("password", ErrorCode.PASSWORD_MISMATCH.getMessage());
        ErrorResponseDTO expected = new ErrorResponseDTO(ErrorCode.PASSWORD_MISMATCH, error);
        initForLogin();

        ResponseEntity<ErrorResponseDTO> result = restTemplate.postForEntity(url, unregistered,
            ErrorResponseDTO.class);

        assertThat(result.getBody()).usingRecursiveComparison().isEqualTo(expected);
    }
}
