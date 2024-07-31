package gift.product.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.domain.Category;
import gift.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@DataJpaTest
@Sql(scripts = "classpath:/static/TestData.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("[ProductRepository] Product 저장")
    void saveProductTest() {
        // given
        Category category = new Category("카테고리", "카테고리 설명", "카테고리 이미지", "카테고리 썸네일 이미지");
        Product product = new Product("테스트 상품", 10000, "https://test.com", category);

        // when
        Product savedProduct = productRepository.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    @DisplayName("[ProductRepository] Product 조회&Paging")
    void getAllProductTest() {
        //given
        Pageable pageable = Pageable.ofSize(5).first();

        //when
        var pageProduct = productRepository.findAll(pageable);

        //then
        assertThat(pageProduct.getContent().size()).isEqualTo(5);
        assertThat(pageProduct.getTotalPages()).isEqualTo(2);
        assertThat(pageProduct.getTotalElements()).isEqualTo(10);
        assertThat(pageProduct.getNumber()).isEqualTo(0);
    }
}