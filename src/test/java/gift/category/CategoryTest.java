package gift.category;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.Category;
import gift.domain.Member;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CategoryTest {

    @LocalServerPort
    private int port;

    private String url;

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    @BeforeEach
    void setUp() {

        url = "http://localhost:" + port;

        Member member = new Member(2L,"admin2@kakao.com", "2222");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Member> requestEntity = new HttpEntity<>(member, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url + "/members/login", member, String.class);

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
    @DisplayName("카테고리 추가")
    @DirtiesContext
    void addCategory() {
        Category category = new Category("상품권","#ffffff","http://gift.jpg","상품권 카테고리");

        HttpEntity<Category> requestEntity = new HttpEntity<>(category, getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/categories", HttpMethod.POST,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("카테고리 조회")
    @DirtiesContext
    void getAllCategory() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/categories", HttpMethod.GET,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("카테고리 단일 조회")
    @DirtiesContext
    void getCategoryById() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/categories/1", HttpMethod.GET,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("카테고리 수정")
    @DirtiesContext
    void updateCategory() {
        Category category = new Category("상품권","#f1f1f1","http://gift2.jpg","상품권 카테고리");

        HttpEntity<Category> requestEntity = new HttpEntity<>(category, getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/categories/1", HttpMethod.PUT,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("카테고리 삭제")
    @DirtiesContext
    void deleteCategory() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/categories/1", HttpMethod.DELETE,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
