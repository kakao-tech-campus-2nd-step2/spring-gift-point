package gift.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.category.CategoryRepository;
import gift.category.Category;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setCategoryRepository() {
        categoryRepository.save(new Category("교환권","쌈@뽕한 블루","www","여름"));
        categoryRepository.save(new Category("과제면제권","방학","www.com","학교"));
        categoryRepository.save(new Category("라우브","스틸더","www.show","키야"));
    }

    @Test
    @DisplayName("상품 저장 테스트")
    void save() {
        Product expected = new Product("사과",2000,"www",categoryRepository.findById(1L).get());
        Product actual = productRepository.save(expected);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("단일 상품 조회 테스트")
    void findById() {
        String expectedName = "사과";
        int expectedPrice = 2000;
        String expectedImageUrl = "www";
        Category category = categoryRepository.findById(1L).get();
        Product expected = new Product(expectedName, expectedPrice, expectedImageUrl, category);
        productRepository.save(expected);
        Product actual = productRepository.findById(expected.getId()).get();
        assertThat(actual).isEqualTo(expected);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getName()).isEqualTo(expectedName),
            () -> assertThat(actual.getPrice()).isEqualTo(expectedPrice),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expectedImageUrl),
            () -> assertThat(actual.getCategory()).isEqualTo(category)
        );
    }

    @Test
    @DisplayName("모든 상품 조회 테스트")
    void findAll() {
        Product product1 = new Product("사과", 2000, "www", categoryRepository.findById(1L).get());
        Product product2 = new Product("앵우", 100000, "www.com", categoryRepository.findById(2L).get());
        Product product3 = new Product("econo", 30000, "error", categoryRepository.findById(3L).get());
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        List<Product> productList = productRepository.findAll();

        assertAll(
            () -> assertThat(productList.size()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void update() {
        Product product = new Product("사과", 2000, "www",categoryRepository.findById(1L).get());

        product.update("바나나", 3000, "www.com");

        assertAll(
            () -> assertThat(product.getName()).isEqualTo("바나나"),
            () -> assertThat(product.getPrice()).isEqualTo(3000),
            () -> assertThat(product.getImageUrl()).isEqualTo("www.com"),
            () -> assertThat(product.getCategory()).isEqualTo(categoryRepository.findById(1L).get())
        );
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void delete() {
        Product product = new Product("사과", 2000, "www", categoryRepository.findById(1L).get());
        productRepository.save(product);
        productRepository.deleteById(product.getId());

        List<Product> isProduct = productRepository.findById(product.getId()).stream().toList();

        assertAll(
            () -> assertThat(isProduct.size()).isEqualTo(0)
        );
    }
}
