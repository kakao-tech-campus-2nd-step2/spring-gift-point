package gift.etoe;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.category.model.CategoryRequest;
import gift.common.model.PageResponseDto;
import gift.member.model.MemberRequest;
import gift.option.model.OptionRequest;
import gift.product.model.ProductRequest;
import gift.wish.model.WishRequest;
import gift.wish.model.WishResponse;
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
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class WishEndToEndTest {

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
    public void getAllWishes() {
        saveWish();

        var wishUrl = "http://localhost:" + port + "/api/wishes";
        var wishRequestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(wishUrl));
        var actual = restTemplate.exchange(wishRequestEntity,
            new ParameterizedTypeReference<PageResponseDto<WishResponse>>() {
            });

        assertThat(actual.getBody()).isEqualTo(
            new PageResponseDto<>(List.of(new WishResponse(1L, 1)), 0, 10
            )
        );
    }

    private void saveWish() {
        var wishUrl = "http://localhost:" + port + "/api/wishes";
        var wishRequest = new WishRequest(1L, 1);
        var wishRequestEntity = new RequestEntity<>(wishRequest, headers, HttpMethod.POST,
            URI.create(wishUrl));
        restTemplate.exchange(wishRequestEntity, String.class);
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

    private void saveCategory(HttpHeaders headers) {
        var categoryUrl = "http://localhost:" + port + "/api/categories";
        var categoryRequest = new CategoryRequest("test", "##test", "test.jpg", "test");
        var categoryRequestEntity = new RequestEntity<>(categoryRequest, headers, HttpMethod.POST,
            URI.create(categoryUrl));
        var categoryResponseEntity = restTemplate.exchange(categoryRequestEntity, String.class);
    }

    private void saveProduct(HttpHeaders headers) {
        var url = "http://localhost:" + port + "/api/products";
        var expected = new ProductRequest.Create("kimchi", 500, "kimchi.jpg", 1L,
            List.of(new OptionRequest.Create("option", 1)));
        var expected1RequestEntity = new RequestEntity<>(expected, headers, HttpMethod.POST,
            URI.create(url));
        restTemplate.exchange(expected1RequestEntity, String.class);
    }
}
