package gift.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.domain.Category;
import gift.domain.Product;
import gift.repository.JpaCategoryRepository;
import gift.repository.JpaProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaProductRepositoryTest {
    @Autowired
    private JpaProductRepository jpaProductRepository;
    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    private Product product;
    private Category category;

    private Long insertProduct(Product product) {
        return jpaProductRepository.save(product).getId();
    }

    @BeforeEach
    void setProduct() {
        category = new Category("교환권", "#6c95d1", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "");
        jpaCategoryRepository.save(category);

        product = new Product("사과", 12000, "www.naver.com", category);
    }

    @Test
    void 상품_저장() {
        //given
        //when
        Long insertProductId = insertProduct(product);
        Product findProduct = jpaProductRepository.findById(insertProductId).get();
        //then
        assertAll(
            () -> assertThat(findProduct).isEqualTo(product)
        );
    }

    @Test
    void 상품_단일_조회() {
        //given
        Long insertProductId = insertProduct(product);
        //when
        Product findProduct = jpaProductRepository.findById(insertProductId).get();
        //then
        assertAll(
            () -> assertThat(findProduct.getId()).isNotNull(),
            () -> assertThat(findProduct.getId()).isEqualTo(insertProductId),
            () -> assertThat(findProduct.getName()).isEqualTo("사과"),
            () -> assertThat(findProduct.getPrice()).isEqualTo(12000),
            () -> assertThat(findProduct.getImageUrl()).isEqualTo("www.naver.com"),

            () -> assertThrows(NoSuchElementException.class,
                () -> jpaProductRepository.findById(100L).get())
        );
    }

    @Test
    void 상품_전체_조회() {
        //given
        Product product1 = new Product("사과", 12000, "www.naver.com", category);
        Product product2 = new Product("바나나", 15000, "www.daum.net", category);
        insertProduct(product1);
        insertProduct(product2);
        //when
        List<Product> productList = jpaProductRepository.findAll();
        //then
        assertAll(
            () -> assertThat(productList).hasSize(2)
        );
    }

    @Test
    void 상품_수정() {
        //given
        insertProduct(product);
        //when
        product.update("바나나", 15000, "www.daum.net", category);
        //then
        assertAll(
            () -> assertThat(product.getName()).isEqualTo("바나나"),
            () -> assertThat(product.getPrice()).isEqualTo(15000),
            () -> assertThat(product.getImageUrl()).isEqualTo("www.daum.net")
        );
    }

    @Test
    void 상품_삭제() {
        //given
        Long insertProductId = insertProduct(product);
        //when
        Product findProduct = jpaProductRepository.findById(insertProductId).get();
        jpaProductRepository.delete(findProduct);
        //then
        List<Product> productList = jpaProductRepository.findAll();
        assertAll(
            () -> assertThat(productList).hasSize(0)
        );
    }
}