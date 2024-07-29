package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.entity.Category;
import gift.entity.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    private Product product1;
    private Product product2;

    private Category category1;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        category1 = categoryRepository.save( new Category("테스트1", "#000000", "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg", ""));

        product1 = productRepository.save(new Product("커피", 10000,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",  categoryRepository.findByName("테스트1").get()));

        product2 = productRepository.save(new Product("커피", 10000,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg", categoryRepository.findByName("테스트1").get()));
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 추가")
    void save() {
        Product expected = productRepository.save(new Product("커피", 10000,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
                category1));
        Product actual = productRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("커피"),
                () -> assertThat(actual.getPrice()).isEqualTo(10000),
                () -> assertThat(actual.getImageUrl()).isNotNull(),
                () -> assertThat(actual.getCategory()).isNotNull()
        );
    }

    @Test
    @DisplayName("상품 조회")
    void findAll(){
        List<Product> actualList = productRepository.findAll();
        assertThat(actualList).containsExactlyInAnyOrder(product1, product2);
    }

    @Test
    @DisplayName("상품 하나 조회")
    void findOne(){
        assertThat(productRepository.findById(product1.getId())).isNotNull();
    }

    @Test
    @DisplayName("상품 수정")
    void update(){
        Product product = productRepository.findById(product1.getId()).get();
        product.changeProduct("커피", 100002,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg", category1);

        productRepository.flush();

        Optional<Product> actual = productRepository.findById(product.getId());

        assertThat(actual.get().getPrice()).isEqualTo(100002);

    }



}