package gift.dto;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.category.CategoryRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CategoryRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    private Set<ConstraintViolation<CategoryRequest>> violations;

    @BeforeEach
    public void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    public void print() {
        for (ConstraintViolation<CategoryRequest> violation : violations) {
            System.out.println(violation.getMessage());
        }
    }

    @Test
    @DisplayName("정상: CategoryRequest")
    public void CategoryRequest_정상() {
        CategoryRequest categoryRequest = new CategoryRequest("카테고리 1", "#000000", "imageUrl",
            "description");
        violations = validator.validate(categoryRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("예외: CategoryRequest 빈 이름")
    public void CategoryRequest_빈_이름() {
        CategoryRequest categoryRequest = new CategoryRequest("", "#000000", "imageUrl",
            "description");
        violations = validator.validate(categoryRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("예외: CategoryRequest 빈 색상 코드")
    public void CategoryRequest_빈_색상코드() {
        CategoryRequest categoryRequest = new CategoryRequest("카테고리 1", "", "imageUrl",
            "description");
        violations = validator.validate(categoryRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("예외: CategoryRequest 잘못된 색상 코드 1")
    public void CategoryRequest_잘못된_색상코드_1() {
        CategoryRequest categoryRequest = new CategoryRequest("카테고리 1", "#00000G", "imageUrl",
            "description");
        violations = validator.validate(categoryRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("예외: CategoryRequest 잘못된 색상 코드 2")
    public void CategoryRequest_잘못된_색상_코드_2() {
        CategoryRequest categoryRequest = new CategoryRequest("카테고리 1", "#0000000", "imageUrl",
            "description");
        violations = validator.validate(categoryRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("예외: CategoryRequest 잘못된 색상코드 3")
    public void CategoryRequest_잘못된_색상코드_3() {
        CategoryRequest categoryRequest = new CategoryRequest("카테고리 1", "#000000 ", "imageUrl",
            "description");
        violations = validator.validate(categoryRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("예외: CategoryRequest 빈 이미지주소")
    public void CategoryRequest_빈_이미지주소() {
        CategoryRequest categoryRequest = new CategoryRequest("카테고리 1", "#000000", "",
            "description");
        violations = validator.validate(categoryRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("정상: CategoryRequest 빈 설명")
    public void CategoryRequest_빈_설명() {
        CategoryRequest categoryRequest = new CategoryRequest("카테고리 1", "#000000", "imageUrl", "");
        violations = validator.validate(categoryRequest);

        assertThat(violations).isEmpty();
    }
}
