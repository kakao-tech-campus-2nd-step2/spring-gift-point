package gift.etoe;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.category.model.CategoryRequest;
import gift.member.model.MemberRequest;
import gift.option.model.OptionRequest;
import gift.product.model.ProductRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class ProductEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllProducts() throws JsonProcessingException {
        var headers = getToken();
        saveCategory(headers);
        var url = "http://localhost:" + port + "/api/products";
        addProduct("gamza", 500, "gamza.jpg", url, headers);
        addProduct("goguma", 1000, "goguma.jpg", url, headers);

        var requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        var actual = restTemplate.exchange(
            requestEntity,
            String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private HttpHeaders getToken() throws JsonProcessingException {
        var tokenUrl = "http://localhost:" + port + "/api/members/register";
        var tokenRequest = new MemberRequest("member1@example.com", "password", "member1",
            "user");
        var tokenRequestEntity = new RequestEntity<>(tokenRequest, HttpMethod.POST,
            URI.create(tokenUrl));
        var tokenResponseEntity = restTemplate.exchange(tokenRequestEntity, String.class);
        var token = objectMapper.readTree(tokenResponseEntity.getBody()).get("accessToken")
            .asText();
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    private void addProduct(String name, Integer price, String imageUrl, String url,
        HttpHeaders headers) {
        var expected = new ProductRequest.Create(name, price, imageUrl, 1L,
            List.of(new OptionRequest.Create("option", 1)));
        var expected1RequestEntity = new RequestEntity<>(expected, headers, HttpMethod.POST,
            URI.create(url));
        restTemplate.exchange(expected1RequestEntity, String.class);
    }

    private void saveCategory(HttpHeaders headers) {
        var categoryUrl = "http://localhost:" + port + "/api/categories";
        var categoryRequest = new CategoryRequest("test", "##test", "test.jpg", "test");
        var categoryRequestEntity = new RequestEntity<>(categoryRequest, headers, HttpMethod.POST,
            URI.create(categoryUrl));
        var categoryResponseEntity = restTemplate.exchange(categoryRequestEntity, String.class);
    }
}
