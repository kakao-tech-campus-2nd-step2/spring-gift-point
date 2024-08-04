package gift.model.product;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.ActiveProfiles;

public class OptionTest {

    private static final String LONG_STRING = "testtesttesttesttesttesttesttesttesttesttesttesttesttest";


    @ParameterizedTest
    @DisplayName("Option_생성_성공_테스트")
    @ValueSource(strings = {"옵션1", "(괄호 가능[])", "빈  칸 3"})
    void option_생성(String testName) {
        // given
        Integer count = 10;
        Product product = createProduct();

        // when
        Option option = new Option(testName, count, product);

        // then
        assertAll(
            () -> assertThat(option.getName()).isEqualTo(testName),
            () -> assertThat(option.getQuantity()).isEqualTo(count)
        );
    }

    @ParameterizedTest
    @DisplayName("Option_실패_이름_유효성_테스트")
    @ValueSource(strings = {LONG_STRING, "특수문자()[]+-&/_만가능!"})
    void option_실패_이름_유효성(String testName) {
        // given
        Integer count = 10;
        Product product = createProduct();

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            new Option(testName, count, product);
        });
    }


    private Product createProduct() {
        return createProduct("test");
    }

    private Product createProduct(String name) {
        Category category = new Category("category", "ABCD", "test", "test");
        Product product = new Product(1L, name, 1000, "product1.jpg", category);

        return product;
    }


}
