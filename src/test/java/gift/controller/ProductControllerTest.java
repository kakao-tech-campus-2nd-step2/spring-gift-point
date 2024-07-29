package gift.controller;/*
package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    private static final String registerUrl = "/api/v1/user/register";
    private static final String loginUrl = "/api/v1/user/login";
    private static final String registerProductUrl = "/api/v1/product";

    @Test
    void addProduct() throws JsonProcessingException {
        CreateProductRequest createProductRequest = new CreateProductRequest("product1", 1000, "image1.jpg", 1L);
        HttpHeaders headers = getToken();
        var url = "http://localhost:" + port + registerProductUrl;
        var requestEntity = new RequestEntity<>(createProductRequest, headers, HttpMethod.POST, URI.create(url));

        var actual = restTemplate.exchange(requestEntity, String.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    public HttpHeaders getToken() {
        var url = "http://localhost:" + port + registerUrl;
        var userRequest = new UserRequest("yso8296", "yso8296@gmail.com");
        var requestEntity = new RequestEntity<>(userRequest, HttpMethod.POST, URI.create(url));
        restTemplate.exchange(requestEntity, String.class);

        url = "http://localhost:" + port + loginUrl;
        requestEntity = new RequestEntity<>(userRequest, HttpMethod.POST, URI.create(url));

        var actual = restTemplate.exchange(requestEntity, String.class);
        var token = actual.getBody();
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return headers;
    }
}
*/
