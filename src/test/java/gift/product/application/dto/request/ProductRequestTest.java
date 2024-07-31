package gift.product.application.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import gift.common.validation.ValidateErrorMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductRequestTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("ProductRequest 생성 테스트[정상]")
    void createProductRequest() {
        // given
        String name = "테스트 상품";
        Integer price = 1000;
        String imgUrl = "http://test.com";
        String category = "카테고리";

        // when
        ProductRequest productRequest = new ProductRequest(name, price, imgUrl, category);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // then
        assertThat(0).isEqualTo(violations.size());
    }

    @Test
    @DisplayName("ProductRequest 생성 테스트[name이 빈 문자열인 경우]")
    void createProductRequestWithNameBlank() {
        // given
        String name = "";
        Integer price = 1000;
        String imgUrl = "http://test.com";
        String category = "카테고리";

        // when
        ProductRequest productRequest = new ProductRequest(name, price, imgUrl, category);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // then
        assertThat(1).isEqualTo(violations.size());
        assertThat(ValidateErrorMessage.INVALID_PRODUCT_NAME_NULL).isEqualTo(
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductRequest 생성 테스트[name이 null 경우]")
    void createProductWithNameNull() {
        // given
        String name = null;
        Integer price = 1000;
        String imgUrl = "http://test.com";
        String category = "카테고리";

        // when
        ProductRequest productRequest = new ProductRequest(name, price, imgUrl, category);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // then
        assertThat(1).isEqualTo(violations.size());
        assertThat(ValidateErrorMessage.INVALID_PRODUCT_NAME_NULL).isEqualTo(
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductRequest 생성 테스트[name이 15자 이상일 경우]")
    void createProductWithNameLengthOver15() {
        // given
        String name = "1234567890123456";
        Integer price = 1000;
        String imgUrl = "http://test.com";
        String category = "카테고리";

        // when
        ProductRequest productRequest = new ProductRequest(name, price, imgUrl, category);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // then
        assertThat(1).isEqualTo(violations.size());
        assertThat(ValidateErrorMessage.INVALID_PRODUCT_NAME_LENGTH)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductRequest 생성 테스트[공백을 포함한 name이 15자 이상인경우]")
    void createProductWithNameLengthOver15WithBlank() {
        // given
        String name = "1234567890 1234 ";
        Integer price = 1000;
        String imgUrl = "http://test.com";
        String category = "카테고리";

        // when
        ProductRequest productRequest = new ProductRequest(name, price, imgUrl, category);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // then
        assertThat(1).isEqualTo(violations.size());
        assertThat(ValidateErrorMessage.INVALID_PRODUCT_NAME_LENGTH)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductRequest 생성 테스트[상품명에 특수기호가 포함된 경우]")
    void createProductWithNameSpecialCharacter() {
        // given
        String name = "테스트 상품!";
        Integer price = 1000;
        String imgUrl = "http://test.com";
        String category = "카테고리";

        // when
        ProductRequest productRequest = new ProductRequest(name, price, imgUrl, category);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // then
        assertThat(1).isEqualTo(violations.size());
        assertThat(ValidateErrorMessage.INVALID_NAME_PATTERN)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductRequest 생성 테스트[가격이 null인 경우]")
    void createProductWithPriceNull() {
        // given
        String name = "테스트 상품";
        Integer price = null;
        String imgUrl = "http://test.com";
        String category = "카테고리";

        // when
        ProductRequest productRequest = new ProductRequest(name, price, imgUrl, category);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // then
        assertThat(1).isEqualTo(violations.size());
        assertThat(ValidateErrorMessage.INVALID_PRODUCT_PRICE_NULL)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductRequest 생성 테스트[가격이 0원 이하 경우]")
    void createProductWithPriceZero() {
        // given
        String name = "테스트 상품";
        Integer price = -1;
        String imgUrl = "http://test.com";
        String category = "카테고리";

        // when
        ProductRequest productRequest = new ProductRequest(name, price, imgUrl, category);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // then
        assertThat(1).isEqualTo(violations.size());
        assertThat(ValidateErrorMessage.INVALID_PRODUCT_PRICE_RANGE)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductRequest 생성 테스트[가격이 21억원 초과인 경우]")
    void createProductWithPriceOverMax() {
        // given
        String name = "테스트 상품";
        Integer price = 2_100_000_001;
        String imgUrl = "http://test.com";
        String category = "카테고리";

        // when
        ProductRequest productRequest = new ProductRequest(name, price, imgUrl, category);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // then
        assertThat(1).isEqualTo(violations.size());
        assertThat(ValidateErrorMessage.INVALID_PRODUCT_PRICE_RANGE)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductRequest 생성 테스트[이미지 URL이 빈 문자열인 경우]")
    void createProductWithImgUrlBlank() {
        // given
        String name = "테스트 상품";
        Integer price = 1000;
        String imgUrl = "";
        String category = "카테고리";

        // when
        ProductRequest productRequest = new ProductRequest(name, price, imgUrl, category);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // then
        assertThat(1).isEqualTo(violations.size());
        assertThat(ValidateErrorMessage.INVALID_PRODUCT_IMG_URL_NULL)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductRequest 생성 테스트[이미지 URL이 null인 경우]")
    void createProductWithImgUrlNull() {
        // given
        String name = "테스트 상품";
        Integer price = 1000;
        String imgUrl = null;
        String category = "카테고리";

        // when
        ProductRequest productRequest = new ProductRequest(name, price, imgUrl, category);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // then
        assertThat(1).isEqualTo(violations.size());
        assertThat(ValidateErrorMessage.INVALID_PRODUCT_IMG_URL_NULL)
                .isEqualTo(violations.iterator().next().getMessage());
    }
}