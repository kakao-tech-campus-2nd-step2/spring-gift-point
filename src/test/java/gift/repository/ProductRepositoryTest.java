package gift.repository;

import gift.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findProductById() {
        Optional<Product> actualProduct = productRepository.findProductById(1000L);
        if (actualProduct.isEmpty()) {
            fail("memberRepository.findMemberById Test Fail");
        }
        assertAll(
                () -> assertThat(actualProduct.get().getId()).isNotNull(),
                () -> assertThat(actualProduct.get().getName()).isEqualTo("아이스 카페 아메리카노 T"),
                () -> assertThat(actualProduct.get().getPrice()).isEqualTo(4500),
                () -> assertThat(actualProduct.get().getImageUrl()).isEqualTo("https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg")
        );
    }
}