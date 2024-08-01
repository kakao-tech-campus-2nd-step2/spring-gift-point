package gift.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import gift.auth.domain.Login;
import gift.domain.Category.CreateCategory;
import gift.domain.Category.UpdateCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryTest {

    @LocalServerPort
    private int port;

    private String url = "http://localhost:";

    private final TestRestTemplate restTemplate;

    private String token;

    private HttpHeaders headers = new HttpHeaders();

    private String commonPath = "/api/category";

    @Autowired
    public CategoryTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    public void setUp() {
        Login login = new Login("kakao1@kakao.com", "1234");

        HttpEntity<Login> requestEntity = new HttpEntity<>(login);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + port + "/api/login",
            POST,
            requestEntity, String.class);

        int startIndex = responseEntity.getBody().indexOf("\"token\":\"") + "\"token\":\"".length();
        int endIndex = responseEntity.getBody().indexOf("\"", startIndex);

        token = responseEntity.getBody().substring(startIndex, endIndex);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

    }

    @Test
    @DisplayName("없는 카테고리 조회")
    public void NotFoundCategoryId() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/1000",
            GET,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("이름이 중복되는 카테고리 생성")
    public void CreateDuplicateCategoryName() {
        CreateCategory body = new CreateCategory("Test1",
            "https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F20240214_EWQEQ.png",
            "축하 리스트",
            "Test222",
            "#B67352");

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);

        restTemplate.exchange(
            url + port + commonPath, POST,
            requestEntity, String.class);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath, POST,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(FORBIDDEN);
    }

    @Test
    @DisplayName("수정될 이름이 기존 카테고리 이름과 중복됨")
    public void UpdateDuplicateCategoryName() {
        UpdateCategory body = new UpdateCategory("aaa",
            "https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F20240214_EWQEQ.png",
            "수정될 카테고리가 존재하지 않음",
            "aaa",
            "#B67352");

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/1", PUT,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(FORBIDDEN);
    }

    @Test
    @DisplayName("수정될 카테고리가 존재하지 않음")
    public void UpdateNotFoundCategory() {
        UpdateCategory body = new UpdateCategory("aaa",
            "https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F20240214_EWQEQ.png",
            "수정될 카테고리가 존재하지 않음",
            "aaa",
            "#B67352");

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/0", PUT,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("삭제될 카테고리가 존재하지 않음")
    public void DeleteNotFoundCategory() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/0", DELETE,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("삭제될 카테고리내 상품이 존재함")
    public void DeleteIsIncludeCategoryInProduct() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/1", DELETE,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(FORBIDDEN);
    }

}