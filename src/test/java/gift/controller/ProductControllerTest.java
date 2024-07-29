package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.category.entity.Category;
import gift.domain.category.repository.CategoryRepository;
import gift.domain.option.dto.OptionRequest;
import gift.domain.product.dto.ProductRequest;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductRepository;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "spring.sql.init.mode=never"
})
class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("상품 전체 조회 테스트")
    void getAllProductsTest() {
        var url = "http://localhost:" + port + "/api/products";
        var request = new RequestEntity<>(HttpMethod.GET, URI.create(url));

        var actual = restTemplate.exchange(request, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("특정 상품 전체 조회 테스트")
    void getProductTest() {
        Category category = new Category("test", "color", "image", "description");
        Category savedCategory = categoryRepository.save(category);

        Product product = new Product("test", 1000, "test.jpg", savedCategory);
        productRepository.save(product);

        var url = "http://localhost:" + port + "/api/products/1";
        var request = new RequestEntity<>(HttpMethod.GET, URI.create(url));

        var actual = restTemplate.exchange(request, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("상품 생성 테스트")
    void createProductTest() {
        Category category = new Category("test", "color", "image", "description");
        categoryRepository.save(category);
        OptionRequest optionRequest = new OptionRequest("name", 100);
        var request = new ProductRequest("product", 1000, "image.jpg", 1L, optionRequest);

        var url = "http://localhost:" + port + "/api/products";
        var requestEntity = new RequestEntity<>(request, HttpMethod.POST, URI.create(url));

        var actual = restTemplate.exchange(requestEntity, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("상품 업데이트 테스트")
    void updateProductTest() {
        Category category = new Category("test", "color", "image", "description");
        Category savedCategory = categoryRepository.save(category);

        Product product = new Product("test", 1000, "test.jpg", savedCategory);
        productRepository.save(product);

        OptionRequest optionRequest = new OptionRequest("name", 100);

        var id = 1L;
        var request = new ProductRequest("update", 1000, "image.jpg", 1L, optionRequest);
        var url = "http://localhost:" + port + "/api/products/" + id;
        var requestEntity = new RequestEntity<>(request, HttpMethod.PUT, URI.create(url));

        var actual = restTemplate.exchange(requestEntity, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void deleteProductTest() {
        Category request = new Category("test", "color", "image", "description");
        Category savedCategory = categoryRepository.save(request);

        productRepository.save(new Product("test", 1000, "test.jpg", savedCategory));

        var id = 1L;
        var url = "http://localhost:" + port + "/api/products/" + id;

        var requestEntity = new RequestEntity<>(HttpMethod.DELETE, URI.create(url));

        var actual = restTemplate.exchange(requestEntity, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}