package gift.model;

import static gift.util.constants.ProductConstants.NAME_INVALID_CHARACTERS;
import static gift.util.constants.ProductConstants.NAME_REQUIRES_APPROVAL;
import static gift.util.constants.ProductConstants.NAME_SIZE_LIMIT;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Product 모델 생성 테스트")
    public void testCreateProduct() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Product1", 100, "imageUrl1", category);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations).isEmpty();
        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("Product1");
        assertThat(product.getPrice()).isEqualTo(100);
        assertThat(product.getImageUrl()).isEqualTo("imageUrl1");
        assertThat(product.getCategoryName()).isEqualTo("Category");
    }

    @Test
    @DisplayName("Product 모델 업데이트 테스트")
    public void testUpdateProduct() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");

        Product product = new Product(1L, "Product1", 100, "imageUrl1", category);
        Category newCategory = new Category(
            "New Category",
            "#111111",
            "newImageUrl",
            "newDescription"
        );

        product.update("Product2", 200, "imageUrl2", newCategory);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations).isEmpty();
        assertThat(product.getName()).isEqualTo("Product2");
        assertThat(product.getPrice()).isEqualTo(200);
        assertThat(product.getImageUrl()).isEqualTo("imageUrl2");
        assertThat(product.getCategoryName()).isEqualTo("New Category");
    }

    @Test
    @DisplayName("유효하지 않은 상품 이름 - 길이 초과")
    public void testInvalidProductNameSize() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(
            1L,
            "가나다라마바사아자차카타파하가나다라마바사아자차카타파하",
            100,
            "imageUrl1",
            category
        );

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("name") &&
                violation.getMessage().equals(NAME_SIZE_LIMIT)
        );
    }

    @Test
    @DisplayName("유효하지 않은 상품 이름 - 특수 문자 포함")
    public void testInvalidProductNamePattern() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Invalid@Name!", 100, "imageUrl1", category);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("name") &&
                violation.getMessage().equals(NAME_INVALID_CHARACTERS)
        );
    }

    @Test
    @DisplayName("유효하지 않은 상품 이름 - '카카오' 포함")
    public void testInvalidProductNameContainsKakao() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "카카오톡", 100, "imageUrl1", category);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("name") &&
                violation.getMessage().equals(NAME_REQUIRES_APPROVAL)
        );
    }
}
