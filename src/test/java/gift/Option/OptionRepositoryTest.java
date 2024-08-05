package gift.Option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.domain.category.Category;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.option.JpaOptionRepository;
import gift.domain.option.Option;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Description;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DataJpaTest
@ActiveProfiles("test")
public class OptionRepositoryTest {

    @Autowired
    JpaProductRepository productRepository;
    @Autowired
    JpaCategoryRepository categoryRepository;
    @Autowired
    JpaOptionRepository optionRepository;
    @Autowired
    EntityManager entityManager;

    private Category category;
    private Product product;
    private Option option;

    @BeforeEach
    void setUp() {
        category = categoryRepository.saveAndFlush(
            new Category("에티오피아산", "에티오피아 산 원두를 사용했습니다.","color code", "http://www.example.com/index.html"));

        product = productRepository.saveAndFlush(new Product("아이스 아메리카노 T", category, 4500,"description",
            "https://example.com/image.jpg"));

        option = optionRepository.saveAndFlush(new Option("에티오피아 커피 옵션1", 289L, product));
    }

    @Test
    @Description("상품의 옵션 조회")
    void getOptions() {
        // when
        Product findProduct = productRepository.findById(product.getId()).orElseThrow();
        List<Option> options = findProduct.getOptions();

        // then
        assertThat(options).hasSize(1);
        assertThat(options.get(0)).isEqualTo(option);
    }

    @Test
    @Description("상품에 옵션 정상 추가")
    void addOption() {
        // given
        Option newOption = new Option("에티오피아 커피 옵션2", 148L, product);

        // when
        Option savedNewOption = optionRepository.saveAndFlush(newOption);
        clear();

        // then
        Product findProduct = productRepository.findById(product.getId()).get();
        assertThat(findProduct.getOptions()).hasSize(2);
        assertThat(findProduct.getOptions().get(1)).isEqualTo(savedNewOption);
    }

    @Test
    @Description("상품 내 옵션 이름 중복 시 에러")
    void addOptionFail() {
        // given
        Option newOption = new Option("에티오피아 커피 옵션1", 148L, product);

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> optionRepository.saveAndFlush(newOption));
    }

    void clear() {
        entityManager.clear();
    }
}
