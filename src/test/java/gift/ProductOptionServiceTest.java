package gift;

import gift.model.ProductOption;
import gift.repository.ProductOptionRepository;
import gift.service.ProductOptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductOptionServiceTest {

    @Mock
    private ProductOptionRepository productOptionRepository;

    @InjectMocks
    private ProductOptionService productOptionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindProductOptionById() {
        ProductOption productOption = new ProductOption();
        productOption.setId(1L);
        productOption.setName("Option 1");

        when(productOptionRepository.findById(anyLong())).thenReturn(Optional.of(productOption));

        ProductOption result = productOptionService.findProductOptionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Option 1", result.getName());
        verify(productOptionRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindProductOptionByIdNotFound() {
        when(productOptionRepository.findById(anyLong())).thenReturn(Optional.empty());

        ProductOption result = productOptionService.findProductOptionById(1L);

        assertNull(result);
        verify(productOptionRepository, times(1)).findById(1L);
    }

    @Test
    public void testSubtractProductOptionQuantity_Success() {
        Long optionId = 1L;
        int initialQuantity = 10;
        int quantityToSubtract = 5;
        ProductOption option = new ProductOption();
        option.setId(optionId);
        option.setName("Test Option");
        option.setQuantity(initialQuantity);

        when(productOptionRepository.findById(optionId)).thenReturn(Optional.of(option));

        productOptionService.subtractProductOptionQuantity(optionId, quantityToSubtract);

        assertEquals(initialQuantity - quantityToSubtract, option.getQuantity());
        verify(productOptionRepository, times(1)).save(option);
    }

    @Test
    public void testSubtractProductOptionQuantity_OptionNotFound() {
        Long optionId = 1L;
        int quantityToSubtract = 5;

        when(productOptionRepository.findById(optionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            productOptionService.subtractProductOptionQuantity(optionId, quantityToSubtract);
        });

        verify(productOptionRepository, never()).save(any(ProductOption.class));
    }

    @Test
    public void testSubtractProductOptionQuantity_InsufficientQuantity() {
        Long optionId = 1L;
        int initialQuantity = 10;
        int quantityToSubtract = 15;
        ProductOption option = new ProductOption();
        option.setId(optionId);
        option.setName("Test Option");
        option.setQuantity(initialQuantity);

        when(productOptionRepository.findById(optionId)).thenReturn(Optional.of(option));

        assertThrows(IllegalArgumentException.class, () -> {
            productOptionService.subtractProductOptionQuantity(optionId, quantityToSubtract);
        });

        assertEquals(initialQuantity, option.getQuantity());
        verify(productOptionRepository, never()).save(option);
    }
}