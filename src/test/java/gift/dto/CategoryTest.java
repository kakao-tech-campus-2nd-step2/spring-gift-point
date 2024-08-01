package gift.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CategoryTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든 입력 값이 유효한 케이스")
    void testCategorySuccess() {
        CategoryDTO categoryDTO = new CategoryDTO(1L, "category", "#FFFFFF", "http://testurl.com/image.png", "description");
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("카테고리 이름이 없는 경우")
    void testCategoryNoName() {
        CategoryDTO categoryDTO = new CategoryDTO(1L, "", "#FFFFFF", "http://testurl.com/image.png", "description");
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("카테고리 이름은 필수 입력 항목입니다.");
    }

    @Test
    @DisplayName("올바르지 않은 색상 코드")
    void testInvalidColor() {
        CategoryDTO categoryDTO = new CategoryDTO(1L, "category", "123", "http://testurl.com/image.png", "description");
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("올바른 HEX 색상 코드로 입력해 주세요.");
    }

    @Test
    @DisplayName("올바르지 않은 이미지 URL")
    void testInvalidImageUrl() {
        CategoryDTO categoryDTO = new CategoryDTO(1L, "category", "#FFF", "invalid_imageUrl", "description");
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("올바른 이미지 URL 형식으로 입력해 주세요.");
    }

    @Test
    @DisplayName("최대 길이를 초과한 설명")
    void testDescriptionTooLong() {
        CategoryDTO categoryDTO = new CategoryDTO(1L, "category", "#FFF", "http://testurl.com/image.png", "description".repeat(25));
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("설명은 최대 255글자 이하로 입력해 주세요.");
    }
}
