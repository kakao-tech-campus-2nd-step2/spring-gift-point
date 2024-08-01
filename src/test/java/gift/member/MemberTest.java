package gift.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

import gift.domain.Member;
import gift.domain.Member.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberTest {

    @LocalServerPort
    private int port;

    private String url;

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    @BeforeEach
    void setUp() {

        url = "http://localhost:" + port;

        MemberRequest member = new MemberRequest("admin@example.com", "1234");

        HttpEntity<MemberRequest> requestEntity = new HttpEntity<>(member);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url + "/api/user/register", member, String.class);

        int startIndex = responseEntity.getBody().indexOf("\"token\":\"") + "\"token\":\"".length();
        int endIndex = responseEntity.getBody().indexOf("\"", startIndex);
        token = responseEntity.getBody().substring(startIndex, endIndex);

    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    @Test
    @DisplayName("로그인 확인")
    @DirtiesContext
    void login() {
        MemberRequest member = new MemberRequest("admin@example.com", "1234");

        HttpEntity<MemberRequest> requestEntity = new HttpEntity<>(member, getHttpHeaders());
        ResponseEntity<String> loginResponse = restTemplate.exchange(url + "/api/user/login", POST, requestEntity, String.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(OK);
    }

}
