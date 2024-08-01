package gift.product;

import static org.assertj.core.api.Assertions.assertThat;


import gift.domain.Member;
import gift.domain.Member.MemberRequest;
import gift.domain.Product;
import gift.domain.Product.ProductRequest;
import gift.domain.Product.ProductResponse;
import gift.util.LoginType;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductTest {

    @LocalServerPort
    private int port;

    private String url;

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    @BeforeEach
    void setUp() {

        url = "http://localhost:" + port;

        MemberRequest member = new MemberRequest("admin@example.com", "1234");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MemberRequest> requestEntity = new HttpEntity<>(member, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
            url + "/api/user/register", requestEntity, String.class);

        int startIndex = responseEntity.getBody().indexOf("\"token\":\"") + "\"token\":\"".length();
        int endIndex = responseEntity.getBody().indexOf("\"", startIndex);
        token = responseEntity.getBody().substring(startIndex, endIndex);
    }

    private HttpHeaders getHttpHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setBearerAuth(token);
        return headers;
    }

    @Test
    @DisplayName("상품 생성")
    @DirtiesContext
    void createProduct() {
        ProductRequest request = new ProductRequest("우유", 1000L, "https://milk.jpg", 1L,null);

        //form data를 받기 때문에
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("name", request.name());
        formData.add("price", String.valueOf(request.price()));
        formData.add("imageUrl", request.imageUrl());

        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(formData, getHttpHeaders(MediaType.APPLICATION_FORM_URLENCODED));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/products", HttpMethod.POST,
            requestEntity, String.class);

        //rediretion으로 인해
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

    @Test
    @DisplayName("상품 조회")
    @DirtiesContext
    void showProductList() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHttpHeaders(MediaType.APPLICATION_JSON));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/products", HttpMethod.GET,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("상품 수정")
    @DirtiesContext
    void updateProduct() {
        ProductRequest request = new ProductRequest("우유", 1000L, "https://example1.jpg", 1L,null);

        //form data를 받기 때문에
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("name", request.name());
        formData.add("price", String.valueOf(request.price()));
        formData.add("imageUrl", request.imageUrl());

        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(formData, getHttpHeaders(MediaType.APPLICATION_FORM_URLENCODED));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/products/1", HttpMethod.PUT,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

    @Test
    @DisplayName("상품 삭제")
    @DirtiesContext
    void deleteProduct() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHttpHeaders(MediaType.APPLICATION_JSON));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/products/1", HttpMethod.DELETE,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

}
