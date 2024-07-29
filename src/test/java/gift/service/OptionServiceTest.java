package gift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.controller.dto.OptionRequest;
import gift.controller.dto.OptionResponse;
import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OptionServiceTest {
    @Mock
    OptionRepository optionRepository;
    @Mock
    ProductRepository productRepository;


    @InjectMocks
    private OptionService optionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("addOption 메서드 확인")
    void addOption() {
        // Given
        Long productId = 1L;
        Product product = new Product("Test Product", 100.0, "image.jpg");
        product.setId(productId);
        // product.setOptions(new ArrayList<>()); // 이 줄은 필요 없습니다.

        OptionRequest optionRequest = new OptionRequest(productId, "New Option", 200);

        Option savedOption = new Option("New Option", 200);
        savedOption.setId(1L);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(optionRepository.save(any(Option.class))).thenReturn(savedOption);

        // When
        OptionResponse response = optionService.addOption(optionRequest);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("New Option", response.getName());
        assertEquals(200, response.getQuantity());

        verify(productRepository).findById(productId);
        verify(optionRepository).save(any(Option.class));
    }
    @Test
    @DisplayName("deleteOption 메서드 확인")
    void deleteOption() {
        // Given
        Long optionId = 1L;
        Option option = new Option("Test Option", 100);
        option.setId(optionId);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        // When
        Long deletedId = optionService.deleteOption(optionId);

        // Then
        assertEquals(optionId, deletedId);
        verify(optionRepository).findById(optionId);
        verify(optionRepository).deleteById(optionId);
    }

    @Test
    @DisplayName("quantity subtract 확인")
    void quantitySubtract(){
        //Given
        Long optionId = 1L;
        Option option = new Option("Test Option", 100);
        option.setId(optionId);

        // When
        option.subtract(99);

        // Then
        assertEquals(1,option.getQuantity());
    }

    @Test
    @DisplayName("quantity subtract 오류상황 확인")
    void errorQuantitySubtract(){
        //Given
        Long optionId = 1L;
        Option option = new Option("Test Option", 100);
        option.setId(optionId);

        // When
        assertThrows(IllegalStateException.class, () -> {
            option.subtract(101);
        });

        // Then
        assertEquals(100, option.getQuantity());


    }
}
