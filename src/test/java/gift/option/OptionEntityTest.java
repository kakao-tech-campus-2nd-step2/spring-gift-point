package gift.option;

import static gift.exception.ErrorMessage.OPTION_NAME_ALLOWED_CHARACTER;
import static gift.exception.ErrorMessage.OPTION_NAME_LENGTH;
import static gift.exception.ErrorMessage.OPTION_QUANTITY_SIZE;
import static gift.exception.ErrorMessage.OPTION_SUBTRACT_NOT_ALLOWED_NEGATIVE_NUMBER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import gift.category.Category;
import gift.option.OptionTestCase.OptionNameAllowedCharacterError;
import gift.option.OptionTestCase.OptionNameLengthError;
import gift.option.OptionTestCase.OptionQuantitySizeError;
import gift.option.entity.Option;
import gift.product.Product;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

public class OptionEntityTest {

    private Product product;
    private long optionId;
    private String optionName;
    private int optionQuantity;
    private Option option;

    @BeforeEach
    void setUp() {
        product = new Product(
            1L,
            "product",
            1,
            "imageUrl",
            new Category(1L, "category")
        );
        optionId = 1L;
        optionName = "optionName";
        optionQuantity = 100;
        option = new Option(
            optionId,
            optionName,
            optionQuantity,
            product
        );
    }

    @Nested
    @DisplayName("[Unit] create entity test")
    class createEntityTest {

        @Test
        @DisplayName("success")
        void testCreateEntity() {
            //when
            option = new Option(
                optionId,
                optionName,
                optionQuantity,
                product
            );

            //then
            assertAll(
                () -> assertEquals(option.getId(), optionId),
                () -> assertEquals(option.getName(), optionName),
                () -> assertEquals(option.getQuantity(), optionQuantity)
            );
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameAllowedCharacterError.class)
        @DisplayName("option name allowed character error")
        void optionNameAllowedError(String optionName) {
            // when & then
            assertThatThrownBy(
                () -> new Option(
                    optionId,
                    optionName,
                    optionQuantity,
                    product
                )
            ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_NAME_ALLOWED_CHARACTER);
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameLengthError.class)
        @DisplayName("option name length error")
        void optionNameLengthError(String optionName) {
            //when & then
            assertThatThrownBy(
                () -> new Option(
                    optionId,
                    optionName,
                    optionQuantity,
                    product
                )
            ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_NAME_LENGTH);
        }

        @ParameterizedTest
        @ArgumentsSource(OptionQuantitySizeError.class)
        @DisplayName("option quantity size error")
        void optionQuantitySizeError(int quantity) {
            // when & then
            assertThatThrownBy(
                () -> new Option(
                    optionId,
                    optionName,
                    quantity,
                    product
                )
            ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_QUANTITY_SIZE);
        }
    }

    @Nested
    @DisplayName("[Unit] update entity test")
    class updateEntityTest {

        @Test
        @DisplayName("success")
        void success() {
            //given
            String updateOptionName = "updateOptionName";
            int updateQuantity = 100;

            //when
            option.update(updateOptionName, updateQuantity);

            //then
            assertAll(
                () -> assertEquals(option.getId(), optionId),
                () -> assertEquals(option.getName(), updateOptionName),
                () -> assertEquals(option.getQuantity(), updateQuantity)
            );
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameAllowedCharacterError.class)
        @DisplayName("option name allowed character error")
        void optionNameAllowedCharacterError(String updateOptionName) {
            // when & then
            assertThatThrownBy(
                () -> option.update(updateOptionName, optionQuantity)
            ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_NAME_ALLOWED_CHARACTER);
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameLengthError.class)
        @DisplayName("option name length error")
        void optionNameLengthError(String updateOptionName) {
            // when & then
            assertThatThrownBy(
                () -> option.update(updateOptionName, optionQuantity)
            ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_NAME_LENGTH);
        }

        @ParameterizedTest
        @ArgumentsSource(OptionQuantitySizeError.class)
        void optionQuantitySizeError(int updateQuantity) {
            // when & then
            assertThatThrownBy(
                () -> option.update(optionName, updateQuantity)
            ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_QUANTITY_SIZE);
        }
    }

    @Nested
    @DisplayName("[Unit] subtract test")
    class subtractTest {

        private int subtractQuantity;

        @BeforeEach
        void setUp() {
            subtractQuantity = 10;
        }

        @Test
        @DisplayName("success")
        void success() {
            //given
            int prevQuantity = option.getQuantity();

            //then
            assertAll(
                () -> assertDoesNotThrow(() -> option.subtract(subtractQuantity)),
                () -> assertEquals(option.getQuantity(), prevQuantity - subtractQuantity)
            );
        }

        @Test
        @DisplayName("subtract number not allowed negative number")
        void subtractNumberNotAllowedNegativeNumber() {
            //given
            subtractQuantity = -1;

            //then
            assertThatThrownBy(() -> option.subtract(subtractQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_SUBTRACT_NOT_ALLOWED_NEGATIVE_NUMBER);
        }
    }

    @Test
    @DisplayName("[Unit] hashCode test")
    void hashCodeTest() {
        //when
        int actual = option.hashCode();

        //then
        assertEquals(optionId, actual);
    }

    @Nested
    @DisplayName("[Unit] equals test")
    class equalsTest {

        @ParameterizedTest
        @MethodSource("equalsSuccess")
        @DisplayName("success")
        void success(Option anotherOption) {
            //then
            assertEquals(option, anotherOption);
        }

        private static Stream<Arguments> equalsSuccess() {
            return Stream.of(
                Arguments.of(new Option(
                    1L,
                    "name",
                    1,
                    new Product(
                        1L,
                        "product",
                        1,
                        "imageUrl",
                        new Category(
                            1L,
                            "category"
                        )
                    )
                )),
                Arguments.of(new Option(
                    1L,
                    "differentOptionName",
                    10,
                    new Product(
                        10L,
                        "differentProductName",
                        100,
                        "differentImageUrl",
                        new Category(
                            1000L,
                            "differentCategory"
                        )
                    )
                ))
            );
        }

        @ParameterizedTest
        @MethodSource("equalsFail")
        @DisplayName("fail")
        void testFail(Object anotherOption) {
            //then
            assertNotEquals(option, anotherOption);
        }

        private static Stream<Arguments> equalsFail() {
            return Stream.of(
                Arguments.of(new Option(
                        2L,
                        "name",
                        1,
                        new Product(
                            1L,
                            "product",
                            1,
                            "imageUrl",
                            new Category(1L, "category")
                        )
                    )
                ),
                Arguments.of(new Object()),
                Arguments.of(""),
                Arguments.of(1)
            );
        }
    }
}
