package gift.product;

import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ProductService.class)
@Transactional
public class ProductPageTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        // Given
        Product product1 = new Product("Ice Americano", 2500, "http://example.com/example.jpg");
        productRepository.save(product1);

        Product product2 = new Product("Caffe Latte", 3500, "http://example.com/example.jpg");
        productRepository.save(product2);

        Product product3 = new Product("Hot Americano", 2000, "http://example.com/example.jpg");
        productRepository.save(product3);

        Product product4 = new Product("Honey Grapefruit Tea", 5500, "http://example.com/example.jpg");
        productRepository.save(product4);

        Product product5 = new Product("Milk Shake", 5000, "http://example.com/example.jpg");
        productRepository.save(product5);
    }

    @Test
    public void testPagination() {
        // When
        Page<Product> page1 = productService.getProductsByPage(0, 2, "price", "desc");
        Page<Product> page2 = productService.getProductsByPage(1, 2, "price", "desc");

        // Then
        // Page 1 assertions
        assertThat(page1.getContent()).hasSize(2);
        assertThat(page1.getContent().get(0).getName()).isEqualTo("Honey Grapefruit Tea");
        assertThat(page1.getContent().get(1).getName()).isEqualTo("Milk Shake");

        // Page 2 assertions
        assertThat(page2.getContent()).hasSize(2);
        assertThat(page2.getContent().get(0).getName()).isEqualTo("Caffe Latte");
        assertThat(page2.getContent().get(1).getName()).isEqualTo("Ice Americano");

        // 전체 페이지 수와 element 수에 대한 추가적인 assertion
        assertThat(page1.getTotalPages()).isEqualTo(3);
        assertThat(page1.getTotalElements()).isEqualTo(5);
    }

    @Test
    public void testPaginationAsc() {
        // When
        Page<Product> page1 = productService.getProductsByPage(0, 2, "price", "asc");
        Page<Product> page2 = productService.getProductsByPage(1, 2, "price", "asc");

        // Then
        // Page 1 assertions
        assertThat(page1.getContent()).hasSize(2);
        assertThat(page1.getContent().get(0).getName()).isEqualTo("Honey Grapefruit Tea");
        assertThat(page1.getContent().get(1).getName()).isEqualTo("Milk Shake");

        // Page 2 assertions
        assertThat(page2.getContent()).hasSize(2);
        assertThat(page2.getContent().get(0).getName()).isEqualTo("Caffe Latte");
        assertThat(page2.getContent().get(1).getName()).isEqualTo("Ice Americano");

        // 전체 페이지 수와 element 수에 대한 추가적인 assertion
        assertThat(page1.getTotalPages()).isEqualTo(3);
        assertThat(page1.getTotalElements()).isEqualTo(5);
    }
}
