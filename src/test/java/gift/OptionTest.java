package gift;

import gift.domain.model.dto.OptionAddRequestDto;
import gift.domain.model.dto.OptionResponseDto;
import gift.domain.model.dto.OptionUpdateRequestDto;
import gift.domain.model.entity.Category;
import gift.domain.model.entity.Option;
import gift.domain.model.entity.Product;
import gift.domain.repository.OptionRepository;
import gift.domain.repository.ProductRepository;
import gift.service.OptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OptionTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OptionService optionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOptionsByProductIdTest() {
        Long productId = 1L;
        Product mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn(productId);

        Option option1 = new Option(mockProduct, "Option 1", 10);
        Option option2 = new Option(mockProduct, "Option 2", 20);
        List<Option> options = Arrays.asList(option1, option2);

        when(optionRepository.findAllByProductId(productId)).thenReturn(options);

        List<OptionResponseDto> result = optionService.getAllOptionsByProductId(productId);

        assertEquals(2, result.size());
        assertEquals("Option 1", result.get(0).getName());
        assertEquals("Option 2", result.get(1).getName());
    }

    @Test
    void validAddOptionTest() {
        Long productId = 1L;
        Product mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn(productId);

        OptionAddRequestDto requestDto = new OptionAddRequestDto("New Option", 30);
        Option savedOption = new Option(mockProduct, "New Option", 30);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(optionRepository.save(any(Option.class))).thenReturn(savedOption);

        OptionResponseDto result = optionService.addOption(productId, requestDto);

        assertNotNull(result);
        assertEquals("New Option", result.getName());
        assertEquals(30, result.getQuantity());
    }

    @Test
    void invalidAddOptionTest() {
        Long productId = 1L;
        OptionAddRequestDto requestDto = new OptionAddRequestDto("New Option", 30);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> optionService.addOption(productId, requestDto));
    }

    @Test
    void validUpdateOptionTest() {
        Long optionId = 1L;
        Product mockProduct = mock(Product.class);
        Option existingOption = new Option(mockProduct, "Old Option", 10);
        OptionUpdateRequestDto requestDto = new OptionUpdateRequestDto("Updated Option", 20);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existingOption));
        when(optionRepository.save(any(Option.class))).thenReturn(existingOption);

        OptionResponseDto result = optionService.updateOption(optionId, requestDto);

        assertNotNull(result);
        assertEquals("Updated Option", result.getName());
        assertEquals(20, result.getQuantity());
    }

    @Test
    void invalidUpdateOptionTest() {
        Long optionId = 1L;
        OptionUpdateRequestDto requestDto = new OptionUpdateRequestDto("Updated Option", 20);

        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> optionService.updateOption(optionId, requestDto));
    }

    @Test
    void validDeleteOptionTest() {
        Long optionId = 1L;

        when(optionRepository.existsById(optionId)).thenReturn(true);

        assertDoesNotThrow(() -> optionService.deleteOption(optionId));
        verify(optionRepository, times(1)).deleteById(optionId);
    }

    @Test
    void inValidDeleteOptionTest() {
        Long optionId = 1L;

        when(optionRepository.existsById(optionId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> optionService.deleteOption(optionId));
    }

    @Test
    void validSubtractOptionQuantityTest() {
        Long optionId = 1L;
        int quantityToSubtract = 5;

        Category mockCategory = mock(Category.class);
        Product mockProduct = mock(Product.class);
        when(mockProduct.getCategory()).thenReturn(mockCategory);

        Option mockOption = new Option(mockProduct, "Test Option", 10);
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(mockOption));
        when(optionRepository.save(any(Option.class))).thenReturn(mockOption);

        OptionResponseDto result = optionService.subtractOptionQuantity(optionId, quantityToSubtract);

        assertNotNull(result);
        assertEquals("Test Option", result.getName());
        assertEquals(5, result.getQuantity());
        verify(optionRepository, times(1)).save(mockOption);
    }

    @Test
    void invalidOptionIdSubtractOptionQuantityTest() {
        Long optionId = 1L;
        int quantityToSubtract = 5;

        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> optionService.subtractOptionQuantity(optionId, quantityToSubtract));
    }

    @Test
    void negativeQuantitySubtractOptionQuantityTest() {
        Long optionId = 1L;
        int quantityToSubtract = -5;

        Category mockCategory = mock(Category.class);
        Product mockProduct = mock(Product.class);
        when(mockProduct.getCategory()).thenReturn(mockCategory);

        Option mockOption = new Option(mockProduct, "Test Option", 10);
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(mockOption));

        assertThrows(IllegalArgumentException.class, () -> optionService.subtractOptionQuantity(optionId, quantityToSubtract));
    }

    @Test
    void insufficientStockSubtractOptionQuantityTest() {
        Long optionId = 1L;
        int quantityToSubtract = 15;

        Category mockCategory = mock(Category.class);
        Product mockProduct = mock(Product.class);
        when(mockProduct.getCategory()).thenReturn(mockCategory);

        Option mockOption = new Option(mockProduct, "Test Option", 10);
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(mockOption));

        assertThrows(IllegalStateException.class, () -> optionService.subtractOptionQuantity(optionId, quantityToSubtract));
    }
}