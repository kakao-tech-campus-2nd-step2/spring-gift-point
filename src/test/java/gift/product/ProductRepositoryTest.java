package gift.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void save() {
        Product expected = new Product(1L, "아메리카노", 1000, "no image", 1L);

        Product actual = productRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    void deleteById() {
        //given
        Long id = 1L;
        Product expected = new Product(1L, "아메리카노", 1000, "no image", 1L);
        Product product = productRepository.save(expected);

        //when
        productRepository.deleteById(product.getId());

        //then
        assertThat(productRepository.findById(id)).isEmpty();
    }
}
