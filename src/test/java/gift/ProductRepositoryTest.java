package gift;

import gift.domain.Category;
import gift.domain.Product;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    private static final String PRODUCT_NAME = "Product Name";
    private static final Integer PRODUCT_PRICE = 1000;
    private static final String PRODUCT_IMAGE_URL = "https://example.com/image.jpg";
    private static final Category PRODUCT_CATEGORY = new Category("Product Category");

    @Test
    void save() {
        // given
        Product product = new Product(PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_IMAGE_URL, PRODUCT_CATEGORY);

        // when
        Product savedProduct = productRepository.save(product);
        Product foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);

        // then
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getId()).isEqualTo(savedProduct.getId());
        assertThat(foundProduct.getName()).isEqualTo(PRODUCT_NAME);
        assertThat(foundProduct.getImageUrl()).isEqualTo(PRODUCT_IMAGE_URL);
        assertThat(foundProduct.getPrice()).isEqualTo(PRODUCT_PRICE);
    }
}


