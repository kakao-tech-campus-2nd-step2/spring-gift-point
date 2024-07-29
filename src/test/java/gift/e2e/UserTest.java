package gift.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import gift.auth.domain.Login;
import gift.domain.User.CreateUser;
import gift.domain.User.UpdateUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTest {

    @LocalServerPort
    private int port;

    private String url = "http://localhost:";

    private final TestRestTemplate restTemplate;

    private String token;

    private HttpHeaders headers = new HttpHeaders();

    private String commonPath = "/api/user";

    @Autowired
    public UserTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    public void setUp() {
        Login login = new Login("kakao1@kakao.com", "1234");

        HttpEntity<Login> requestEntity = new HttpEntity<>(login);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/login",
            POST,
            requestEntity, String.class);

        int startIndex = responseEntity.getBody().indexOf("\"token\":\"") + "\"token\":\"".length();
        int endIndex = responseEntity.getBody().indexOf("\"", startIndex);

        token = responseEntity.getBody().substring(startIndex, endIndex);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

    }

    @Test
    @DisplayName("유저 수정")
    public void UpdateUser() {
        UpdateUser body = new UpdateUser("123456789");

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/1",
            PUT, requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    @DisplayName("유저 수정(유저 없음)")
    public void NotFoundUpdateUser() {
        UpdateUser body = new UpdateUser("123456789");

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/0",
            PUT, requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("유저 생성")
    public void CreateUser() {
        CreateUser body = new CreateUser("kakao10@kakao.com", "1234");

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + commonPath,
            POST, requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    //    병렬로 수행하면 오류나지만, 단독으로 수행하면 수행 됨
    @Test
    @DisplayName("유저 생성(유저 중복)")
    public void DuplicateCreateUser() {
        CreateUser body = new CreateUser("kakao1@kakao.com", "1234");

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + commonPath,
            POST,
            requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(FORBIDDEN);
    }

    @Test
    @DisplayName("유저 삭제")
    public void removeUser() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/2",
            DELETE, requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    @DisplayName("유저 삭제(유저 없음)")
    public void NotFoundRemoveUser() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/0",
            DELETE, requestEntity, String.class);

        System.out.println(responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

}