package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.model.Category;
import gift.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product1;
    private Product product2;
    private Product savedProduct;
    private Category category;
    private Category savedCategory;
    private Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    public void setUp() {
        category = new Category(1L, "교환권");
        savedCategory = categoryRepository.save(category);
        product1 = new Product(1L, "상품", "100", savedCategory, "https://kakao");
        product2 = new Product(2L, "상품2", "200", savedCategory, "https://kakao2");
        savedProduct = productRepository.save(product1);
        productRepository.save(product2);
    }

    @Test
    void testSave() {
        assertAll(
            () -> assertThat(savedProduct.getId()).isNotNull(),
            () -> assertThat(savedProduct.getName()).isEqualTo(product1.getName()),
            () -> assertThat(savedProduct.getPrice()).isEqualTo(product1.getPrice()),
            () -> assertThat(savedProduct.getCategory().getId()).isEqualTo(category.getId()),
            () -> assertThat(savedProduct.getImageUrl()).isEqualTo(product1.getImageUrl())
        );
    }

    @Test
    void testFindAll() {
        Page<Product> products = productRepository.findAll(pageable);
        assertAll(
            () -> assertThat(products.getTotalElements()).isEqualTo(2),
            () -> assertThat(products.getContent().get(0).getId()).isEqualTo(product1.getId()),
            () -> assertThat(products.getContent().get(1).getId()).isEqualTo(product2.getId())
        );
    }

    @Test
    void testFindById() {
        Product foundProduct = productRepository.findById(savedProduct.getId()).get();
        assertAll(
            () -> assertThat(foundProduct).isNotNull(),
            () -> assertThat(foundProduct.getId()).isEqualTo(savedProduct.getId())
        );
    }

    @Test
    void testDelete() {
        productRepository.deleteById(savedProduct.getId());
        boolean exists = productRepository.existsById(savedProduct.getId());
        assertThat(exists).isFalse();
    }
}