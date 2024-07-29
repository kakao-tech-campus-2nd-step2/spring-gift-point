package gift.product;

import gift.core.domain.product.Product;
import gift.core.domain.product.ProductCategory;
import gift.product.infrastructure.persistence.repository.JpaProductCategoryRepository;
import gift.product.infrastructure.persistence.repository.JpaProductRepository;
import gift.product.infrastructure.persistence.entity.ProductCategoryEntity;
import gift.product.infrastructure.persistence.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {
    @Autowired
    private JpaProductCategoryRepository jpaProductCategoryRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    private ProductCategoryEntity category;

    @BeforeEach
    public void setUp() {
        category = jpaProductCategoryRepository.save(
                ProductCategoryEntity.fromDomain(new ProductCategory(0L, "test"))
        );
    }

    @Test
    public void saveProduct() {
        ProductEntity product = jpaProductRepository.save(ProductEntity.fromDomain(sampleProduct()));

        assertThat(jpaProductRepository.findById(product.getId())).isPresent();
        assertThat(jpaProductRepository.findById(product.getId()).get()).isEqualTo(product);
    }

    @Test
    public void findProductById() {
        ProductEntity product = jpaProductRepository.save(ProductEntity.fromDomain(sampleProduct()));

        assertThat(jpaProductRepository.findById(product.getId())).isPresent();
    }

    @Test
    public void deleteProduct() {
        ProductEntity product = jpaProductRepository.save(ProductEntity.fromDomain(sampleProduct()));

        jpaProductRepository.deleteById(product.getId());

        assertThat(jpaProductRepository.findById(product.getId())).isEmpty();
    }

    private Product sampleProduct() {
        return new Product(0L, "test", 100, "test", category.toDomain());
    }
}
