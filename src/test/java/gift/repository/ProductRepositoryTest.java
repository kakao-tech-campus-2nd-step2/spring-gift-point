package gift.repository;

import gift.model.category.Category;
import gift.model.gift.Product;
import gift.model.option.Option;
import gift.repository.gift.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품이 조회가 잘 되는지 테스트")
    void testfindById() {
        Product product = productRepository.findById(1L).orElseThrow(IllegalArgumentException::new);
        assertThat(product.getName()).isEqualTo("coffee");
    }

    @Test
    @DisplayName("상품 저장이 잘 되는지 테스트")
    void testSave() {
        Category category = new Category(10L, "test", "test", "test", "test");
        Option option1 = new Option("testOption", 1);
        List<Option> option = Arrays.asList(option1);
        Product product = new Product("test", 1000, "abc.jpg", category, option);
        Product actual = productRepository.save(product);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(product.getName())
        );
    }


}