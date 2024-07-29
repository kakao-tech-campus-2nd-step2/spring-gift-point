package gift.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import gift.exception.InputException;
import gift.exception.option.OptionsQuantityException;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OptionsTest {


    @DisplayName("옵션 파라미터 검증 테스트")
    @Test
    void validation() {
        String name = "옵션";
        Integer quantity = 1;
        Product product = demoProduct();

        assertDoesNotThrow(() -> new Options(name, quantity, product));
    }

    @DisplayName("옵션 파라미터 검증 실패 테스트")
    @Test
    void failValidation() {
        Product validProduct = demoProduct();
        Integer validQuantity = 1;
        String validName = "이름()";

        // Options name 오류
        List<String> errorNames = Arrays.asList(null, "", "a".repeat(51), "#");
        errorNames.forEach(name -> {
            assertThrowsExactly(
                InputException.class, () -> new Options(name, validQuantity, validProduct));
        });

        // Options quantity 오류
        List<Integer> errorQuantitys = Arrays.asList(null, -1, 100000001);
        errorQuantitys.forEach(quantity -> {
            assertThrowsExactly(InputException.class,
                () -> new Options(validName, quantity, validProduct));
        });

        // Options product 오류
        List<Product> errorProducts = Arrays.asList(null,
            new Product(null, "상품", 1000, "http://a.com", new Category(1L, "카테고리")));
        errorProducts.forEach(product -> {
            assertThrowsExactly(InputException.class,
                () -> new Options(validName, validQuantity, product));
        });
    }

    @DisplayName("옵션 수량 차감 테스트")
    @Test
    void subtract() {
        //given
        Product product = demoProduct();
        Integer originalQuantity = 1;
        Options option = new Options(1L, "옵션", originalQuantity, product);
        Integer subQuantity = 1;

        //when //then
        assertDoesNotThrow(() -> option.subtractQuantity(subQuantity));
        assertThat(option.getQuantity()).isEqualTo(originalQuantity - subQuantity);

    }

    @DisplayName("옵션 수량 차감 실패 테스트")
    @Test
    void failSubtract() {
        //given
        Product product = demoProduct();
        Integer originalQuantity = 1;
        Options option = new Options(1L, "옵션", originalQuantity, product);
        Integer subQuantity = 2;

        //when //then
        assertThatThrownBy(() -> option.subtractQuantity(subQuantity))
            .isInstanceOf(OptionsQuantityException.class);

    }


    private static Product demoProduct() {
        return new Product(1L, "상품", 1000, "http://a.com", new Category(1L, "카테고리"));
    }
}
