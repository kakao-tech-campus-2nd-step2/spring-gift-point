package gift.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/initialize.sql"})
public class KakaoUserTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void Port() {
        assertThat(port).isNotZero();
    }

    @Test
    @DisplayName("kakao login")
    void kakaoLogin() {
        // given
        var url = "http://localhost:" + port + "/users/kakao/login";
        var request = new RequestEntity<>(HttpMethod.GET, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, String.class);

        // then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getStatusCode().is3xxRedirection()).isTrue();
    }
}
