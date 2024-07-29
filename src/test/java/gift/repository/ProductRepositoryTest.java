package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Category;
import gift.entity.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product1;
    private Product product2;
    private Category category;
    private Category category2;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        category = new Category("테스트 교환권", "#6c95d1",
            "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "");
        category2 = new Category("테스트 상품권", "#6c95d1",
            "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "");
        categoryRepository.save(category);
        categoryRepository.save(category2);

        product1 = new Product("Product1", 1000, "http://example.com/image1.jpg", category);
        product2 = new Product("Product2", 2000, "http://example.com/image2.jpg", category2);
        productRepository.save(product1);
        productRepository.save(product2);
    }

    @Test
    @DisplayName("아이디로 상품 찾기")
    void testFindById() {
        Optional<Product> product = productRepository.findById(product1.getId());
        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo("Product1");
    }

    @Test
    @DisplayName("모든 상품 찾기")
    void testFindAll() {
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2);
    }

    @Test
    @DisplayName("새로운 상품 저장")
    void testSaveNewProduct() {
        Product newProduct = new Product("Product3", 3000, "http://example.com/image3.jpg",
            category);
        Product savedProduct = productRepository.save(newProduct);

        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Product3");
    }

    @Test
    @DisplayName("상품 수정")
    void testSaveUpdateProduct() {
        product1.updateProduct("UpdateProduct", 1000, "http://example.com/image1.jpg", category);
        productRepository.save(product1);

        Optional<Product> updateProduct = productRepository.findById(product1.getId());
        assertThat(updateProduct).isPresent();
        assertThat(updateProduct.get().getName()).isEqualTo("UpdateProduct");
    }

    @Test
    @DisplayName("상품 삭제")
    void testDeleteById() {
        productRepository.delete(product2);
        List<Product> product = productRepository.findAll();
        assertThat(product).hasSize(1);
    }
}