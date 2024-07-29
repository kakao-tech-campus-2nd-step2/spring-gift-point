package gift.intergrationTest;

import static org.assertj.core.api.Assertions.assertThat;

import gift.controller.auth.AuthController;
import gift.controller.auth.LoginRequest;
import gift.controller.auth.Token;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ApplicationContext context;
    @LocalServerPort
    private int port;


    void LoginTest() {
        // register
        String url = "http://localhost:" + port + "/api/members/register";
        LoginRequest loginRequest = new LoginRequest("hongsik1234@kakao.com", "password1234");
        RequestEntity<LoginRequest> requestEntity = new RequestEntity<>(loginRequest,
            HttpMethod.POST, URI.create(url));
        ResponseEntity<Token> registerResponse = restTemplate.exchange(requestEntity, Token.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // login
        url = "http://localhost:" + port + "/login";
        requestEntity = new RequestEntity<>(loginRequest, HttpMethod.POST, URI.create(url));
        ResponseEntity<Token> loginResponse = restTemplate.exchange(requestEntity, Token.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
