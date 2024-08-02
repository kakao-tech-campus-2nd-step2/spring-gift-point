package gift.Repository;

import gift.Entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveProduct() {
        ProductEntity product = new ProductEntity();
        product.setName("Test Product");
        product.setPrice(100);

        ProductEntity savedProduct = productRepository.save(product);

        assertProduct(savedProduct, "Test Product", 100);
    }

    @Test
    public void testFindByName() {
        ProductEntity product = new ProductEntity();
        product.setName("Unique Product");
        product.setPrice(200);
        productRepository.save(product);

        Optional<ProductEntity> foundProduct = productRepository.findByName("Unique Product");

        assertThat(foundProduct).isPresent();
        assertProduct(foundProduct.get(), "Unique Product", 200);
    }

    private void assertProduct(ProductEntity product, String expectedName, int expectedPrice) {
        assertThat(product).isNotNull();
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isEqualTo(expectedName);
        assertThat(product.getPrice()).isEqualTo(expectedPrice);
    }
}
