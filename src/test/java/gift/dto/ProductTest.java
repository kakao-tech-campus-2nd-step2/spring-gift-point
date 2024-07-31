package gift.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든 입력 값이 유효한 케이스")
    public void testValidProductDTO() {
        ProductDTO productDTO = new ProductDTO(1L, "테스트상품", 1000, "http://example.com/image.jpg", 1L, List.of(1L, 2L));

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("1자 이하의 상품명")
    public void testProductNameTooShort() {
        ProductDTO productDTO = new ProductDTO(1L, "", 1000, "http://example.com/image.jpg", 1L, List.of(1L, 2L));

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("상품의 이름은 1자 15자 이내로 작성해주세요.");
    }

    @Test
    @DisplayName("15자 이상의 상품명")
    public void testProductNameTooLong() {
        ProductDTO productDTO = new ProductDTO(1L, "product".repeat(3), 1000, "http://example.com/image.jpg", 1L, List.of(1L, 2L));

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("상품의 이름은 1자 15자 이내로 작성해주세요.");
    }

    @Test
    @DisplayName("'카카오'라는 글자 가 포함된 상품명")
    public void testProductNameContainsKakao() {
        ProductDTO productDTO = new ProductDTO(1L, "카카오상품", 1000, "http://example.com/image.jpg", 1L, List.of(1L, 2L));

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("'카카오'가 포함된 문구는 담당 MD와 협의 후 사용 바랍니다.");
    }

    @Test
    @DisplayName("음수인 상품 가격")
    public void testNegativePrice() {
        ProductDTO productDTO = new ProductDTO(1L, "테스트상품", -1000, "http://example.com/image.jpg", 1L, List.of(1L, 2L));

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("가격은 0 이상의 숫자를 입력해 주세요.");
    }

    @Test
    @DisplayName("올바르지 않은 이미지 URL")
    public void testInvalidImageUrl() {
        ProductDTO productDTO = new ProductDTO(1L, "테스트상품", 1000, "invalid-url", 1L, List.of(1L, 2L));

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("올바른 이미지 URL 형식으로 입력해 주세요.");
    }

    @Test
    @DisplayName("비어있는 카테고리 ID")
    public void testNullCategoryId() {
        ProductDTO productDTO = new ProductDTO(1L, "테스트상품", 1000, "http://example.com/image.jpg", null, List.of(1L, 2L));

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("카테고리는 필수 항목입니다.");
    }
}