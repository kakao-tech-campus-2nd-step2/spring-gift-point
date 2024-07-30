package gift.etoe;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.category.model.CategoryRequest;
import gift.member.model.MemberRequest;
import gift.option.model.OptionRequest;
import gift.option.model.OptionRequest.Create;
import gift.option.model.OptionResponse;
import gift.product.model.ProductRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class OptionEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        headers = getToken();
        saveCategory(headers);
        saveProduct(headers);
    }

    @Test
    void getOptions() {
        var url = "http://localhost:" + port + "/api/products/1/options";
        var requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        var actual = restTemplate.exchange(requestEntity,
            new ParameterizedTypeReference<List<OptionResponse>>() {
            });
        assertThat(actual.getBody()).isEqualTo(List.of(new OptionResponse(1L, "option", 1)));
    }

    @Test
    void addOption() {
        var url = "http://localhost:" + port + "/api/products/1/options";
        var request = new OptionRequest.Create("option1", 2);
        var requestEntity = new RequestEntity<>(request, headers, HttpMethod.POST, URI.create(url));
        var actual = restTemplate.exchange(requestEntity, String.class);
        System.out.println(actual);
    }

    @Test
    void updateOption() {
        var url = "http://localhost:" + port + "/api/options/1";
        var request = new OptionRequest.Update("option1", 2);
        var requestEntity = new RequestEntity<>(request, headers, HttpMethod.PUT, URI.create(url));
        var actual = restTemplate.exchange(requestEntity, String.class);
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        return httpHeaders;
    }

    private void saveCategory(HttpHeaders headers) {
        var categoryUrl = "http://localhost:" + port + "/api/categories";
        var categoryRequest = new CategoryRequest("test", "##test", "test.jpg", "test");
        var categoryRequestEntity = new RequestEntity<>(categoryRequest, headers, HttpMethod.POST,
            URI.create(categoryUrl));
        restTemplate.exchange(categoryRequestEntity, String.class);
    }

    private void saveProduct(HttpHeaders headers) {
        var url = "http://localhost:" + port + "/api/products";
        var expected = new ProductRequest.Create("kimchi", 500, "kimchi.jpg", 1L,
            List.of(new Create("option", 1)));
        var expected1RequestEntity = new RequestEntity<>(expected, headers, HttpMethod.POST,
            URI.create(url));
        restTemplate.exchange(expected1RequestEntity, String.class);
    }
}
