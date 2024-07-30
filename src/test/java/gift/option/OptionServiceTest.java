package gift.option;

import static gift.exception.ErrorMessage.OPTION_ALREADY_EXISTS;
import static gift.exception.ErrorMessage.OPTION_NAME_ALLOWED_CHARACTER;
import static gift.exception.ErrorMessage.OPTION_NAME_LENGTH;
import static gift.exception.ErrorMessage.OPTION_NOT_FOUND;
import static gift.exception.ErrorMessage.OPTION_QUANTITY_SIZE;
import static gift.exception.ErrorMessage.OPTION_SUBTRACT_NOT_ALLOWED_NEGATIVE_NUMBER;
import static gift.exception.ErrorMessage.PRODUCT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.category.entity.Category;
import gift.option.OptionTestCase.OptionNameAllowedCharacterError;
import gift.option.OptionTestCase.OptionNameLengthError;
import gift.option.OptionTestCase.OptionQuantitySizeError;
import gift.option.dto.OptionRequestDTO;
import gift.option.entity.Option;
import gift.product.entity.Product;
import gift.product.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = OptionService.class)
public class OptionServiceTest {

    @MockBean
    private OptionRepository optionRepository;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private OptionService optionService;

    // given values
    private long productId;
    private Product product;
    private OptionRequestDTO optionDTO;
    private Option option;

    @BeforeEach
    void setUp() {
        productId = 1L;

        product = new Product(
            1L,
            "product",
            1,
            "imageUrl",
            new Category(1L, "category")
        );

        optionDTO = new OptionRequestDTO(
            1L,
            "option-1",
            100
        );

        option = new Option(
            1L,
            "option-1",
            100,
            product
        );
    }

    @Nested
    @DisplayName("[Unit] get options test")
    class getOptionsTest {

        private List<Option> options;

        @BeforeEach
        void setUp() {
            //given
            options = List.of(
                new Option(1L, "option-1", 1, product),
                new Option(2L, "option-2", 2, product),
                new Option(3L, "option-3", 3, product)
            );

            //when
            when(productRepository.existsById(productId))
                .thenReturn(true);

            when(optionRepository.findAllByProductId(productId))
                .thenReturn(options);
        }

        @Test
        @DisplayName("success")
        void success() {
            //given
            List<OptionRequestDTO> expected = List.of(
                new OptionRequestDTO(1L, "option-1", 1),
                new OptionRequestDTO(2L, "option-2", 2),
                new OptionRequestDTO(3L, "option-3", 3)
            );

            //when
            List<OptionRequestDTO> actual = optionService.getOptions(productId);

            //then
            assertEquals(actual, expected);
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() {
            //given
            options = List.of();

            //when
            when(productRepository.existsById(productId))
                .thenReturn(false);

            //then
            assertThatThrownBy(() -> optionService.getOptions(productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PRODUCT_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("[Unit] add option test")
    class addOptionTest {

        @BeforeEach
        void setUp() {
            //when
            when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

            when(optionRepository.existsByNameAndProductId(optionDTO.getName(), productId))
                .thenReturn(false);

            when(optionRepository.existsById(optionDTO.getId()))
                .thenReturn(false);

            when(optionRepository.save(option))
                .thenReturn(option);
        }

        @Test
        @DisplayName("success")
        void success() {
            //then
            assertDoesNotThrow(() -> optionService.addOption(productId, optionDTO));
            verify(optionRepository, times(1)).save(option);
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() {
            //when
            when(productRepository.findById(productId))
                .thenReturn(Optional.empty());
            //then
            assertThatThrownBy(() -> optionService.addOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PRODUCT_NOT_FOUND);
        }

        @Test
        @DisplayName("option name already exists error")
        void optionNameAlreadyExistsError() {
            //when
            when(optionRepository.existsByNameAndProductId(optionDTO.getName(), productId))
                .thenReturn(true);

            //then
            assertThatThrownBy(() -> optionService.addOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_ALREADY_EXISTS);
        }

        @Test
        @DisplayName("option already exists error")
        void optionAlreadyExistsError() {
            //when
            when(optionRepository.existsById(optionDTO.getId()))
                .thenReturn(true);

            //then
            assertThatThrownBy(() -> optionService.addOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_ALREADY_EXISTS);
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameAllowedCharacterError.class)
        @DisplayName("option name allowed character error")
        void optionNameAllowedCharacterError(String optionName) {
            //given
            optionDTO = new OptionRequestDTO(
                1L,
                optionName,
                1
            );

            //then
            assertThatThrownBy(() -> optionService.addOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_NAME_ALLOWED_CHARACTER);
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameLengthError.class)
        @DisplayName("option name length error")
        void optionNameLengthError(String optionName) {
            //given
            optionDTO = new OptionRequestDTO(
                1L,
                optionName,
                1
            );

            //then
            assertThatThrownBy(() -> optionService.addOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_NAME_LENGTH);
        }

        @ParameterizedTest
        @ArgumentsSource(OptionQuantitySizeError.class)
        @DisplayName("option quantity size error")
        void optionQuantitySizeError(int quantity) {
            //given
            optionDTO = new OptionRequestDTO(
                1L,
                "option-1",
                quantity
            );

            //then
            assertThatThrownBy(() -> optionService.addOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_QUANTITY_SIZE);
        }
    }

    @Nested
    @DisplayName("[Unit] update option test")
    class updateOptionTest {

        @BeforeEach
        void setUp() {
            //given
            optionDTO = new OptionRequestDTO(
                1L,
                "update-option",
                2
            );

            //when
            when(productRepository.existsById(productId))
                .thenReturn(true);

            when(optionRepository.findById(optionDTO.getId()))
                .thenReturn(Optional.of(option));

            when(optionRepository.existsByNameAndProductId(optionDTO.getName(), productId))
                .thenReturn(false);

            when(optionRepository.save(option))
                .thenReturn(option);
        }

        @Test
        @DisplayName("success")
        void success() {
            //then
            assertAll(
                () -> assertDoesNotThrow(() -> optionService.updateOption(productId, optionDTO)),
                () -> assertEquals(optionDTO.getId(), option.getId()),
                () -> assertEquals(optionDTO.getName(), option.getName()),
                () -> assertEquals(optionDTO.getQuantity(), option.getQuantity())
            );

            verify(optionRepository, times(1)).save(option);
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() {
            //when
            when(productRepository.existsById(productId))
                .thenReturn(false);

            //then
            assertThatThrownBy(() -> optionService.updateOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PRODUCT_NOT_FOUND);
        }

        @Test
        @DisplayName("option not found error")
        void optionNotFoundError() {
            //when
            when(optionRepository.findById(optionDTO.getId()))
                .thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> optionService.updateOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_NOT_FOUND);
        }

        @Test
        @DisplayName("option already exists error")
        void optionAlreadyExistsError() {
            //when
            when(optionRepository.existsByNameAndProductId(optionDTO.getName(), productId))
                .thenReturn(true);

            //then
            assertThatThrownBy(() -> optionService.updateOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_ALREADY_EXISTS);
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameAllowedCharacterError.class)
        @DisplayName("option name allowed character error")
        void optionNameAllowedCharacterError(String optionName) {
            //given
            optionDTO = new OptionRequestDTO(
                1L,
                optionName,
                1
            );

            //then
            assertThatThrownBy(() -> optionService.updateOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_NAME_ALLOWED_CHARACTER);
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameLengthError.class)
        @DisplayName("option name length error")
        void optionNameLengthError(String optionName) {
            //given
            optionDTO = new OptionRequestDTO(
                1L,
                optionName,
                1
            );

            //then
            assertThatThrownBy(() -> optionService.updateOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_NAME_LENGTH);
        }

        @ParameterizedTest
        @ArgumentsSource(OptionQuantitySizeError.class)
        @DisplayName("option quantity size error")
        void optionQuantitySizeError(int quantity) {
            //given
            optionDTO = new OptionRequestDTO(
                1L,
                "update-option",
                quantity
            );

            //then
            assertThatThrownBy(() -> optionService.updateOption(productId, optionDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_QUANTITY_SIZE);
        }
    }

    @Nested
    @DisplayName("[Unit] delete option test")
    class deleteOptionTest {

        private long optionId;

        @BeforeEach
        void setUp() {
            //given
            optionId = 1L;

            //when
            when(productRepository.existsById(productId))
                .thenReturn(true);

            when(optionRepository.findById(optionId))
                .thenReturn(Optional.of(option));

            doNothing().when(optionRepository)
                .delete(option);
        }

        @Test
        @DisplayName("success")
        void success() {
            //then
            assertDoesNotThrow(() -> optionService.deleteOption(productId, optionId));
            verify(optionRepository, times(1)).delete(option);
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() {
            //when
            when(productRepository.existsById(productId))
                .thenReturn(false);

            //then
            assertThatThrownBy(() -> optionService.deleteOption(productId, optionId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PRODUCT_NOT_FOUND);
        }

        @Test
        @DisplayName("option not found error")
        void optionNotFoundError() {
            //when
            when(optionRepository.findById(optionId))
                .thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> optionService.deleteOption(productId, optionId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("[Unit] subtract option test")
    class subtractOptionTest {

        private int prevOptionQuantity;
        private int subtractOptionQuantity;
        private long optionId;

        @BeforeEach
        void setUp() {
            //given
            prevOptionQuantity = option.getQuantity();
            subtractOptionQuantity = 5;
            optionId = 1L;

            //when
            when(productRepository.existsById(productId))
                .thenReturn(true);

            when(optionRepository.findById(optionDTO.getId()))
                .thenReturn(Optional.of(option));

            when(optionRepository.save(option))
                .thenReturn(option);
        }

        @Test
        @DisplayName("success")
        void success() {
            //then
            assertAll(
                () -> assertDoesNotThrow(
                    () -> optionService.subtract(
                        optionId,
                        subtractOptionQuantity
                    )
                ), () -> assertEquals(
                    option.getQuantity(),
                    prevOptionQuantity - subtractOptionQuantity
                )
            );

            //verify
            verify(optionRepository, times(1)).save(option);
        }

        @Test
        @DisplayName("option subtract not allow negative number error")
        void optionSubtractNegativeNumberError() {
            //given
            subtractOptionQuantity = -100;

            //then
            assertThatThrownBy(
                () -> optionService.subtract(
                    optionId,
                    subtractOptionQuantity
                )
            ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_SUBTRACT_NOT_ALLOWED_NEGATIVE_NUMBER);
        }

        @Test
        @DisplayName("option not found error")
        void optionNotFoundError() {
            //when
            when(optionRepository.findById(optionId))
                .thenReturn(Optional.empty());

            //then
            assertThatThrownBy(
                () -> optionService.subtract(
                    optionId,
                    subtractOptionQuantity
                )
            ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_NOT_FOUND);
        }

        @Test
        @DisplayName("subtract quantity too big error")
        void subtractQuantityTooBigError() {
            //given
            subtractOptionQuantity = 1234567;

            //then
            assertThatThrownBy(
                () -> optionService.subtract(
                    optionId,
                    subtractOptionQuantity
                )
            ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OPTION_QUANTITY_SIZE);
        }
    }
}
