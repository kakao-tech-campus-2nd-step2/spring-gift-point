package gift.repository;

import gift.vo.Category;
import gift.vo.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category savedCategory ;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        Category category = new Category(null, "테스트 카테고리", "#6c95d1", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "this is test category");
        savedCategory = categoryRepository.save(category);

        Product product = new Product(
                savedCategory,
                "Ice Americano",
                4200,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");
        savedProduct = productRepository.save(product); // Product pre-save
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll(); // Clean up after each test
    }

    @Test
    @DisplayName("Product save")
    void save() {
        // given
        Product newProduct = new Product(
                savedCategory,
                "Ice Americano",
                4200,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");
        // when
        Product actual = productRepository.save(newProduct);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(newProduct.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(newProduct.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(newProduct.getImageUrl())
        );
    }

    @Test
    @DisplayName("Find All Product")
    void findAllProduct() {
        // given
        Product product2 = productRepository.save(new Product(
                savedCategory,
                "Ice Americano",
                4200,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"));
        // when
        List<Product> products = productRepository.findAll();

        // then
        assertAll(
                () ->  assertThat(products.size()).isEqualTo(2),
                () -> assertThat(products.contains(savedProduct)).isTrue(),
                () -> assertThat(products.contains(product2)).isTrue()
        );
    }

    @Test
    @DisplayName("Find Product by wishId")
    void findProductById() {
        // given
        Long expected = savedProduct.getId();

        // when
        Optional<Product> actual = productRepository.findById(expected);

        // then
        assertTrue(actual.isPresent());
        assertThat(actual.get().getId()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Delete Product by id")
    void deleteProductById() {
        // when
        productRepository.deleteById(savedProduct.getId());
        boolean exists = productRepository.existsById(savedProduct.getId());

        // then
        assertThat(exists).isFalse();
    }

}
