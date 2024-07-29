package gift.dao;

import gift.product.dao.OptionRepository;
import gift.product.dao.ProductRepository;
import gift.product.entity.Category;
import gift.product.entity.Option;
import gift.product.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import testFixtures.CategoryFixture;
import testFixtures.OptionFixture;
import testFixtures.ProductFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        Category category = CategoryFixture.createCategory("상품권");
        product = ProductFixture.createProduct("product", category);
        productRepository.save(product);
    }

    @Test
    @DisplayName("옵션 추가 및 옵션 상품 ID 및 이름 조회 테스트")
    void saveAndFindByProductIdAndName() {
        Option option = OptionFixture.createOption("옵션", product);

        Option savedOption = optionRepository.save(option);
        Option foundOption = optionRepository.findByProduct_IdAndName(
                product.getId(),
                savedOption.getName()
        ).orElse(null);

        assertThat(foundOption).isNotNull();
        assertThat(foundOption.equals(savedOption)).isTrue();
    }

    @Test
    @DisplayName("옵션 상품 ID 및 이름 조회 실패 테스트")
    void findByProductIdAndNameFailed() {
        Option option = OptionFixture.createOption("옵션", product);
        optionRepository.save(option);

        Option foundOption = optionRepository.findByProduct_IdAndName(
                product.getId(),
                "invalid_option"
        ).orElse(null);

        assertThat(foundOption).isNull();
    }

    @Test
    @DisplayName("옵션 삭제 테스트")
    void deleteOption() {
        Option option = OptionFixture.createOption("옵션", product);
        Option savedOption = optionRepository.save(option);

        optionRepository.deleteById(savedOption.getId());

        boolean exists = optionRepository.existsById(savedOption.getId());
        assertThat(exists).isFalse();
    }

}