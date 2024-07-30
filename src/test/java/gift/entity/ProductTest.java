package gift.entity;

import gift.exception.OptionDuplicateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("상품 엔티티 테스트")
class ProductTest {

    @Test
    @DisplayName("옵션 이름 중복 검사 - 같은 이름이 존재하여 실패")
    void checkDuplicateOptionNameFail() {
        //Given
        Product product = new Product("name", 100, "url", new Category("Food", "t", "t", "t"), List.of(new Option("option", 100)));
        String newOptionName = "option";

        //When Then
        assertThatThrownBy(() -> product.checkDuplicateOptionName(newOptionName))
                .isInstanceOf(OptionDuplicateException.class);
    }

    @Test
    @DisplayName("옵션 이름 중복 검사 - 성공")
    void checkDuplicateOptionNameSuccess() {
        //Given
        Option option = new Option("option", 100);
        Product product = new Product("name", 100, "url", new Category("Food", "t", "t", "t"), List.of(option));
        option.associateWithProduct(product);

        String newOptionName = "otherOptionName";

        //When
        product.checkDuplicateOptionName(newOptionName);

        //Then
        assertDoesNotThrow(() -> product.checkDuplicateOptionName(newOptionName));
    }
}
