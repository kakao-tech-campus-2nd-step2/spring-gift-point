package gift.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.dto.request.CreateProductRequest;
import gift.product.dto.request.NewOption;
import gift.product.dto.response.ProductResponse;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/initialize.sql", "/sql/insert_categories.sql",
    "/sql/insert_products.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ProductTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void Port() {
        assertThat(port).isNotZero();
    }

    @Test
    @DisplayName("get all products test")
    void getAllProductsTest() {
        // given
        var url = "http://localhost:" + port + "/api/products";
        var request = new RequestEntity<>(HttpMethod.GET, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, String.class);

        // then
        assertThat(actualResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(actualResponse.getBody()).isNotNull();
    }

    @Test
    @DisplayName("get id 1L product test")
    void getId1LProductTest() {
        // given
        var url = "http://localhost:" + port + "/api/products/1";
        var request = new RequestEntity<>(HttpMethod.GET, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, ProductResponse.class);

        // then
        assertThat(actualResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody().id()).isEqualTo(1L);
        assertThat(actualResponse.getBody().name()).isEqualTo("Product A");
        assertThat(actualResponse.getBody().price()).isEqualTo(1000);
        assertThat(actualResponse.getBody().imageUrl()).isEqualTo(
            "http://example.com/images/product_a.jpg");
        assertThat(actualResponse.getBody().categoryName()).isEqualTo("Category 1");
    }

    @Test
    @DisplayName("get product by not exist id test")
    void getProductByNotExistIdTest() {
        // given
        var url = "http://localhost:" + port + "/api/products/999";
        var request = new RequestEntity<>(HttpMethod.GET, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, ProductResponse.class);

        // then
        assertThat(actualResponse.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    @DisplayName("create product test")
    void createProductTest() {
        // given
        NewOption option = new NewOption("option 1", 100);
        var url = "http://localhost:" + port + "/api/products";
        var requestBody = new CreateProductRequest("new product", 10000, "link", 2L,
            List.of(option));
        var request = new RequestEntity<>(requestBody, HttpMethod.POST, URI.create(url));

        var expectedLocation = URI.create("/api/products/6");

        // when
        var actualResponse = restTemplate.exchange(request, String.class);

        // then
        assertThat(actualResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(actualResponse.getHeaders().getLocation()).isEqualTo(expectedLocation);
    }

    @Test
    @DisplayName("delete product test")
    void deleteProductTest() {
        // given
        var url = "http://localhost:" + port + "/api/products/1";
        var request = new RequestEntity<>(HttpMethod.DELETE, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, String.class);

        // then
        assertThat(actualResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    @DisplayName("delete not exist product test")
    void deleteNotExistProductTest() {
        // given
        var url = "http://localhost:" + port + "/api/products/999";
        var request = new RequestEntity<>(HttpMethod.DELETE, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, String.class);

        // then
        assertThat(actualResponse.getStatusCode().is4xxClientError()).isTrue();
    }
}
