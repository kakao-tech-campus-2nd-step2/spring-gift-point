package gift.option;

import gift.category.model.Category;
import gift.category.repository.CategoryRepository;
import gift.option.domain.Option;
import gift.option.repository.OptionJpaRepository;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OptionEntityTest {

    @Autowired
    private Validator validator;

    @Autowired
    private OptionJpaRepository optionJpaRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository  categoryRepository;

    @Test
    @Transactional
    void 옵션_이름_길이_최대50자() {
        // Given: 이름이 50자를 초과하는 옵션 객체가 주어졌을 때
        Option option = new Option("option".repeat(50), 100L);

        // When: 옵션 객체를 유효성 검증할 때
        Set<ConstraintViolation<Option>> violations = validator.validate(option);

        // Then: 유효성 검증에 실패하고, 길이 제한 위반 메시지를 포함한 제약 조건 위반이 발생해야 한다
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("옵션 이름은 공백을 포함하여 최대 50자입니다.");
    }


    @Test
    @Transactional
    void 옵션_이름_특수문자_제한() {
        // Given: 이름에 허용되지 않는 특수 문자가 포함된 옵션 객체가 주어졌을 때
        Option option = new Option("@이름!!!!!", 100L);

        // When: 옵션 객체를 유효성 검증할 때
        Set<ConstraintViolation<Option>> violations = validator.validate(option);

        // Then: 유효성 검증에 실패하고, 특수 문자 제한 위반 메시지를 포함한 제약 조건 위반이 발생해야 한다
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("특수 문자 중 ()[]+-&/_만 사용 가능하며, 영어와 한국어만 허용됩니다.");
    }

    @Test
    @Transactional
    void 옵션_수량_최소값_검증() {
        // Given: 수량이 0인 옵션 객체가 주어졌을 때
        Option option = new Option("ValidName", 0L);

        // When: 옵션 객체를 유효성 검증할 때
        Set<ConstraintViolation<Option>> violations = validator.validate(option);

        // Then: 유효성 검증에 실패하고, 수량 최소값 위반 메시지를 포함한 제약 조건 위반이 발생해야 한다
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("Quantity은 최소 1개 이상이어야 합니다.");
    }


    @Test
    @Transactional
    void 옵션_수량_최대값_검증() {
        // Given: 수량이 1억 이상인 옵션 객체가 주어졌을 때
        Option option = new Option("ValidName", 100000000L);

        // When: 옵션 객체를 유효성 검증할 때
        Set<ConstraintViolation<Option>> violations = validator.validate(option);

        // Then: 유효성 검증에 실패하고, 수량 최대값 위반 메시지를 포함한 제약 조건 위반이 발생해야 한다
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("Quantity는 최대 1억 미만 개까지 가능합니다.");
    }


    @Test
    @Transactional
    void 유효성_검증() {
        // Given: 유효한 카테고리, 제품 및 옵션 객체가 주어졌을 때
        Category category = new Category("category");
        categoryRepository.save(category);
        Product product = new Product("product", 1000, "image", category);
        productRepository.save(product);

        Option option = new Option("ValidName", 100L, product);
        product.addOption(option);

        // When: 옵션 객체를 저장할 때
        Option savedOption = optionJpaRepository.save(option);

        // Then: 유효성 검증에 성공하고, 저장된 옵션 객체가 올바른 값을 가지고 있어야 한다
        Set<ConstraintViolation<Option>> violations = validator.validate(option);
        assertThat(violations).isEmpty();
        assertNotNull(savedOption);
        assertThat(savedOption.getName()).isEqualTo("ValidName");
        assertThat(savedOption.getQuantity()).isEqualTo(100L);
    }

}
