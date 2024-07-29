package gift.product;

import gift.core.domain.product.ProductOption;
import gift.core.domain.product.ProductOptionRepository;
import gift.core.domain.product.ProductOptionService;
import gift.core.domain.product.ProductRepository;
import gift.core.domain.product.exception.OptionAlreadExistsException;
import gift.core.domain.product.exception.OptionLimitExceededException;
import gift.core.domain.product.exception.ProductNotFoundException;
import gift.core.exception.validation.InvalidArgumentException;
import gift.product.service.ProductOptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductOptionServiceTests {
    @Mock
    private ProductOptionRepository productOptionRepository;

    @Mock
    private ProductRepository productRepository;

    private ProductOptionService productOptionService;

    @BeforeEach
    public void setUp() {
        productOptionService = new ProductOptionServiceImpl(
                productOptionRepository,
                productRepository,
                100_000_000L
        );
    }

    @Test
    public void testRegisterOptionToProduct() {
        Long productId = 1L;

        ProductOption productOption = new ProductOption(1L, "test", 100);

        when(productRepository.exists(1L)).thenReturn(true);
        when(productOptionRepository.hasOption(1L)).thenReturn(false);
        when(productOptionRepository.hasOption(1L, "test")).thenReturn(false);
        when(productOptionRepository.countByProductId(1L)).thenReturn(0L);

        productOptionService.registerOptionToProduct(productId, productOption);
        verify(productOptionRepository).save(productId, productOption);
    }

    @Test
    @DisplayName("존재하지 않는 상품에 옵션을 등록할 경우")
    public void testRegisterOptionToProductWhenProductDoesNotExist() {
        Long productId = 1L;

        ProductOption productOption = new ProductOption(1L, "test", 100);

        when(productRepository.exists(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> {
            productOptionService.registerOptionToProduct(productId, productOption);
        });
    }

    @Test
    @DisplayName("이미 존재하는 옵션을 등록할 경우")
    public void testRegisterOptionToProductWithExistingOption() {
        Long productId = 1L;

        ProductOption productOption = new ProductOption(1L, "test", 100);

        when(productRepository.exists(1L)).thenReturn(true);
        when(productOptionRepository.hasOption(1L)).thenReturn(true);

        assertThrows(OptionAlreadExistsException.class, () -> {
            productOptionService.registerOptionToProduct(productId, productOption);
        });
    }

    @Test
    @DisplayName("이미 존재하는 옵션명을 등록할 경우")
    public void testRegisterOptionToProductWithExistingOptionName() {
        Long productId = 1L;

        ProductOption productOption = new ProductOption(1L, "test", 100);

        when(productRepository.exists(1L)).thenReturn(true);
        when(productOptionRepository.hasOption(1L)).thenReturn(false);
        when(productOptionRepository.hasOption(1L, "test")).thenReturn(true);

        assertThrows(OptionAlreadExistsException.class, () -> {
            productOptionService.registerOptionToProduct(productId, productOption);
        });
    }

    @Test
    @DisplayName("옵션 등록 제한을 초과할 경우")
    public void testRegisterOptionToProductWithExceedingOptionLimit() {
        Long productId = 1L;

        ProductOption productOption = new ProductOption(1L, "test", 100);

        when(productRepository.exists(1L)).thenReturn(true);
        when(productOptionRepository.hasOption(1L)).thenReturn(false);
        when(productOptionRepository.hasOption(1L, "test")).thenReturn(false);
        when(productOptionRepository.countByProductId(1L)).thenReturn(100_000_000L);

        assertThrows(OptionLimitExceededException.class, () -> {
            productOptionService.registerOptionToProduct(productId, productOption);
        });
    }

    @Test
    @DisplayName("옵션 수량이 0 이하인 경우")
    public void testRegisterOptionToProductWithNegativeQuantity() {
        Long productId = 1L;

        ProductOption productOption = new ProductOption(1L, "test", -1);

        when(productRepository.exists(1L)).thenReturn(true);
        when(productOptionRepository.hasOption(1L)).thenReturn(false);
        when(productOptionRepository.hasOption(1L, "test")).thenReturn(false);
        when(productOptionRepository.countByProductId(1L)).thenReturn(0L);

        assertThrows(InvalidArgumentException.class, () -> {
            productOptionService.registerOptionToProduct(productId, productOption);
        });
    }

    @Test
    @DisplayName("옵션 수량을 차감")
    public void testSubtractQuantityFromOption() {
        Long optionId = 1L;
        Integer quantity = 10;

        when(productOptionRepository.findById(optionId)).thenReturn(Optional.of(sampleOption()));

        productOptionService.subtractQuantityFromOption(optionId, quantity);
        verify(productOptionRepository).save(any(), any());
    }

    @Test
    @DisplayName("옵션 수량을 음수로 차감할 경우")
    public void testSubtractQuantityFromOptionWithNegativeQuantity() {
        Long optionId = 1L;
        Integer quantity = -1;

        when(productOptionRepository.findById(optionId)).thenReturn(Optional.of(sampleOption()));

        assertThrows(InvalidArgumentException.class, () -> {
            productOptionService.subtractQuantityFromOption(optionId, quantity);
        });
    }

    @Test
    @DisplayName("옵션 수량을 초과하여 차감할 경우")
    public void testSubtractQuantityFromOptionWithExceedingQuantity() {
        Long optionId = 1L;
        Integer quantity = 101;

        when(productOptionRepository.findById(optionId)).thenReturn(Optional.of(sampleOption()));

        assertThrows(InvalidArgumentException.class, () -> {
            productOptionService.subtractQuantityFromOption(optionId, quantity);
        });
    }

    private ProductOption sampleOption() {
        return new ProductOption(1L, "test", 100);
    }
}
