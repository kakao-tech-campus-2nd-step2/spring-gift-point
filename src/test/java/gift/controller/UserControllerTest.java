package gift.controller;/*
package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String registerUrl = "/api/v1/user/register";
    private static final String loginUrl = "/api/v1/user/login";

    @Test
    @DisplayName("회원가입")
    void register() {
        var url = "http://localhost:" + port + registerUrl;
        var userRequest = new UserRequest("yso8296", "yso8296@gmail.com");
        var requestEntity = new RequestEntity<>(userRequest, HttpMethod.POST, URI.create(url));

        var actual = restTemplate.exchange(requestEntity, String.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("로그인")
    void login() {
        var url = "http://localhost:" + port + registerUrl;
        var userRequest = new UserRequest("yso8296", "yso8296@gmail.com");
        var requestEntity = new RequestEntity<>(userRequest, HttpMethod.POST, URI.create(url));
        restTemplate.exchange(requestEntity, String.class);

        url = "http://localhost:" + port + loginUrl;
        requestEntity = new RequestEntity<>(userRequest, HttpMethod.POST, URI.create(url));
        var actual = restTemplate.exchange(requestEntity, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
*/
