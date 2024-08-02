package gift.etoe;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.category.model.CategoryRequest;
import gift.category.model.CategoryResponse;
import gift.member.model.MemberRequest;
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
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@ActiveProfiles("test")
public class CategoryEndToEndTest {

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
    }

    @Test
    void getCategories() {
        var url = "http://localhost:" + port + "/api/categories";
        var request = new CategoryRequest("category", "##cate", "category", "category");
        var requestEntity = new RequestEntity<>(request, headers, HttpMethod.POST, URI.create(url));
        restTemplate.exchange(requestEntity, String.class);
        var requestEntity2 = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        var actual = restTemplate.exchange(requestEntity2,
            new ParameterizedTypeReference<List<CategoryResponse>>() {
            });
        assertThat(actual.getBody()).isEqualTo(
            List.of(new CategoryResponse(1L, "category", "##cate", "category", "category")));
    }

    private HttpHeaders getToken() throws JsonProcessingException {
        var tokenUrl = "http://localhost:" + port + "/api/members/register";
        var tokenRequest = new MemberRequest("member1@example.com", "password");
        var tokenRequestEntity = new RequestEntity<>(tokenRequest, HttpMethod.POST,
            URI.create(tokenUrl));
        var tokenResponseEntity = restTemplate.exchange(tokenRequestEntity, String.class);
        var token = objectMapper.readTree(tokenResponseEntity.getBody()).get("accessToken")
            .asText();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        return httpHeaders;
    }
}
