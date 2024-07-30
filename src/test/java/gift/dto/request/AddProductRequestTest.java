package gift.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 추가 요청 DTO 테스트")
class AddProductRequestTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("옵션 이름 중복")
    void testValidAddProductRequest1() {
        //Given
        AddProductRequest request = new AddProductRequest(
                "ValidName",
                1000,
                "http://example.com/image.jpg",
                1L,
                List.of(new OptionRequest("Option1", 101),
                        new OptionRequest("Option1", 1))
        );

        //When
        Set<ConstraintViolation<AddProductRequest>> violations = validator.validate(request);

        //Then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("상품의 옵션 이름은 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("옵션 0개")
    void testValidAddProductRequest2() {
        //Given
        AddProductRequest request = new AddProductRequest(
                "ValidName",
                1000,
                "http://example.com/image.jpg",
                1L,
                new ArrayList<>()
        );

        //When
        Set<ConstraintViolation<AddProductRequest>> violations = validator.validate(request);

        //Then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("상품에는 하나 이상의 옵션이 있어야합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "15자____________", "    햇반      ", "[단독] 고급 지갑", "커피&우유", "1+1 제품", "/바로/구매___", "-_- 안사면 후회"})
    @DisplayName("유효한 상품 이름")
    void testValidAddProductRequest3(String name) {
        //Given
        AddProductRequest request = new AddProductRequest(
                name,
                1000,
                "http://example.com/image.jpg",
                1L,
                List.of(new OptionRequest("Option1", 101),
                        new OptionRequest("Option2", 1))
        );

        //When
        Set<ConstraintViolation<AddProductRequest>> violations = validator.validate(request);

        //Then
        assertThat(violations.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"!!!", "저렴한 우유!!", "@멘션", "#더샵", "진짜~~~~", "카카오톡", "카카오 우유", "카카오카카오", "16자_____________", "15자 넘는 공백              포함"})
    @DisplayName("비유효한 상품 이름")
    void testValidAddProductRequest4(String name) {
        //Given
        AddProductRequest request = new AddProductRequest(
                name,
                1000,
                "http://example.com/image.jpg",
                1L,
                List.of(new OptionRequest("Option1", 101),
                        new OptionRequest("Option2", 1))
        );

        //When
        Set<ConstraintViolation<AddProductRequest>> violations = validator.validate(request);

        //Then
        assertThat(violations.isEmpty()).isFalse();
    }
}
