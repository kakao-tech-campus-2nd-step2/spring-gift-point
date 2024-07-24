package gift.database;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.GiftOption;
import gift.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class JpaProductRepositoryTest {

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    @Autowired
    private JpaGiftOptionRepository jpaGiftOptionRepository;

    @Test
    @DisplayName("product 확인하기")
    void test1() {

        //given
        Product product = new Product(null, "test1", 123, "abc");
        GiftOption giftOption = new GiftOption(null, "test2", 124);
        product.addGiftOption(giftOption);

        //when
        jpaProductRepository.save(product);
        giftOption = jpaGiftOptionRepository.save(giftOption);

        GiftOption giftOption2 = new GiftOption(giftOption.getId(), "test3", 12);
        jpaGiftOptionRepository.save(giftOption2);

        //then
        assertThat(jpaProductRepository.findAll()).hasSize(1);
        assertThat(jpaGiftOptionRepository.findAll()).hasSize(1);
        assertThat(jpaProductRepository.findById(product.getId()).orElseThrow().getGiftOptionList().getFirst().getName()).isEqualTo(giftOption2.getName());

    }


}