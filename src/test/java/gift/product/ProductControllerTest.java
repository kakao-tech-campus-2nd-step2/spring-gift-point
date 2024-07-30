package gift.product;


import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.RequestEntity;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment =  WebEnvironment.RANDOM_PORT)
class ProductControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void port() {
        assertThat(port).isNotZero();
    }

    @Test
    void create() {
        var url = "http://localhost:" + port + "/api/products";
        var request = new ProductRequestDto("사과",2000,"www", 1L);
        var requestEntity = new RequestEntity<>(request, POST, URI.create(url));
        var actual = restTemplate.exchange(requestEntity, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void readAll() {
        var url = "http://localhost:" + port + "/api/products";
        var actual = restTemplate.getForEntity(url,String.class);
        assertThat(actual.getStatusCode()).isEqualTo(OK);
    }

    private String baseUrl;
    private Long productId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/products";

        // 각 테스트 전에 제품 생성
        var request = new ProductRequestDto("사과", 2000, "www", 1L);
        var requestEntity = new RequestEntity<>(request, POST, URI.create(baseUrl));
        var createResponse = restTemplate.exchange(requestEntity, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(OK);

        // 실제 구현에서는 생성된 제품의 ID를 응답에서 파싱해야 합니다.
        // 여기서는 간단히 1L로 가정합니다.
        productId = 1L;
    }

    @Test
    void read() {
        //when
        Long productId = 1L;

        var readUrl = "http://localhost:" + port + "/api/products/" + productId;
        var response = restTemplate.getForEntity(readUrl, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void delete() {
        //when
        Long productId = 1L;

        var deleteUrl = "http://localhost:" + port + "/api/products/" + productId;
        restTemplate.delete(deleteUrl);
        var response = restTemplate.getForEntity(deleteUrl, String.class);

        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);

    }

    @Test
    void update() {
        // when
        Long productId = 1L;

        var updateUrl = "http://localhost:" + port + "/api/products/" + productId;
        var update = new ProductRequestDto("파김치", 10000, "www.com", 1L);
        var requestUpdate = new RequestEntity<>(update, PUT, URI.create(updateUrl));
        var updateResponse = restTemplate.exchange(requestUpdate, String.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(OK);
    }
}
