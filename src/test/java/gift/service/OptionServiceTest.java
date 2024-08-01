package gift.service;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.OptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubtractOptionQuantity() {
        Long productId = 1L;
        Long optionId = 1L;
        int quantityToSubtract = 5;

        Category category = new Category("Test Category");
        Product product = new Product(productId, "Test Product", 1000, "test.jpg", category);
        Option option = new Option(optionId, "Test Option", 10, product);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        optionService.subtractOptionQuantity(productId, optionId, quantityToSubtract);

        assertEquals(5, option.getQuantity());
        verify(optionRepository, times(1)).save(option);
    }

    @Test
    public void testSubtractOptionQuantity_NotEnoughQuantity() {
        Long productId = 1L;
        Long optionId = 1L;
        int quantityToSubtract = 15;

        Category category = new Category("Test Category");
        Product product = new Product(productId, "Test Product", 1000, "test.jpg", category);
        Option option = new Option(optionId, "Test Option", 10, product);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            optionService.subtractOptionQuantity(productId, optionId, quantityToSubtract);
        });

        assertEquals("남아있는 수량이 더 작습니다.", exception.getMessage());
    }

    @Test
    public void testSubtractOptionQuantity_InvalidProductId() {
        Long productId = 2L;
        Long optionId = 1L;
        int quantityToSubtract = 5;

        Category category = new Category("Test Category");
        Product product = new Product(1L, "Test Product", 1000, "test.jpg", category);
        Option option = new Option(optionId, "Test Option", 10, product);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            optionService.subtractOptionQuantity(productId, optionId, quantityToSubtract);
        });

        assertEquals("Option does not belong to the given product", exception.getMessage());
    }
}
