package gift.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import gift.domain.auth.Login;
import gift.domain.product.ProductOption.CreateOption;
import gift.domain.product.ProductOption.UpdateOption;
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
class ProductOptionTest {

    @LocalServerPort
    private int port;

    private String url = "http://localhost:";

    private final TestRestTemplate restTemplate;

    private String token;

    private HttpHeaders headers = new HttpHeaders();

    private String commonPath = "/api/products/1/options";

    @Autowired
    public ProductOptionTest(TestRestTemplate restTemplate) {
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
    @DisplayName("없는 상품 옵션 조회")
    public void NotFoundProductOptionId() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/0",
            GET,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("이름이 중복되는 상품 옵션 생성")
    public void CreateDuplicateProductOptionName() {
        CreateOption body = new CreateOption("duplicate", 100L);

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
    @DisplayName("이름이 중복되는 상품 옵션 생성")
    public void CreateNotFoundProduct() {
        CreateOption body = new CreateOption("duplicate", 100L);

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + "/api/products/0/options", POST,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("수정될 상품 옵션이 존재하지 않음")
    public void UpdateNotFoundProductOption() {
        UpdateOption body = new UpdateOption("aaa", 100L);

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/0", PUT,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("수정될 이름이 기존 상품옵션 이름과 중복됨")
    public void UpdateDuplicateProductOptionName() {
        UpdateOption body = new UpdateOption("option20", 100L);

        HttpEntity<Long> requestEntity = new HttpEntity(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/1", PUT,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(UNAUTHORIZED);
    }


    @Test
    @DisplayName("삭제될 상품옵션이 존재하지 않음")
    public void DeleteNotFoundProductOption() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/0", DELETE,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("상품에 옵션이 하나만 존재해서 삭제가 안됨")
    public void DeleteIsIncludeCategoryInProduct() {
        HttpEntity<Long> requestEntity = new HttpEntity(null, headers);

        restTemplate.exchange(
            url + port + commonPath + "/7", DELETE,
            requestEntity, String.class);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            url + port + commonPath + "/1", DELETE,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(UNAUTHORIZED);
    }

}