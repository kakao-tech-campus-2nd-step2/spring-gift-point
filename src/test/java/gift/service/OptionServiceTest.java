package gift.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import gift.controller.dto.OptionRequest;
import gift.controller.dto.OptionResponse;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.utils.error.OptionNotFoundException;
import gift.utils.error.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class OptionServiceTest {

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
    @DisplayName("addOption 메서드 확인")
    void addOption() {
        // Given
        Long productId = 1L;
        Product product = new Product("Test Product", 100, "image.jpg");
        product.setId(productId);

        OptionRequest optionRequest = new OptionRequest("New Option", 200);

        Option savedOption = new Option("New Option", 200);
        savedOption.setId(1L);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(optionRepository.save(any(Option.class))).thenReturn(savedOption);

        // When
        OptionResponse response = optionService.addOption(optionRequest, productId);

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
        String email = "test@example.com";
        Option option = new Option("Test Option", 100);
        option.setId(optionId);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        // When
        Long deletedId = optionService.deleteOption(optionId, email);

        // Then
        assertEquals(optionId, deletedId);
        verify(optionRepository).findById(optionId);
        verify(optionRepository).deleteById(optionId);
    }

    @Test
    @DisplayName("changeOption 메서드 확인")
    void changeOption() {
        // Given
        Long productId = 1L;
        Long optionId = 1L;
        Product product = new Product("Test Product", 100, "image.jpg");
        product.setId(productId);

        Option existingOption = new Option("Existing Option", 100);
        existingOption.setId(optionId);

        OptionRequest optionRequest = new OptionRequest("Updated Option", 150);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existingOption));
        when(optionRepository.save(any(Option.class))).thenReturn(existingOption);

        // When
        OptionResponse response = optionService.changeOption(optionRequest, productId, optionId);

        // Then
        assertNotNull(response);
        assertEquals(optionId, response.getId());
        assertEquals("Updated Option", response.getName());
        assertEquals(150, response.getQuantity());

        verify(productRepository).findById(productId);
        verify(optionRepository).findById(optionId);
        verify(optionRepository).save(any(Option.class));
    }

    @Test
    @DisplayName("addOption 메서드 - 상품 없을 때 예외 처리")
    void addOptionProductNotFound() {
        // Given
        Long productId = 1L;
        OptionRequest optionRequest = new OptionRequest("New Option", 200);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> optionService.addOption(optionRequest, productId));
    }

    @Test
    @DisplayName("deleteOption 메서드 - 옵션 없을 때 예외 처리")
    void deleteOptionNotFound() {
        // Given
        Long optionId = 1L;
        String email = "test@example.com";

        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OptionNotFoundException.class, () -> optionService.deleteOption(optionId, email));
    }

    @Test
    @DisplayName("changeOption 메서드 - 상품 또는 옵션 없을 때 예외 처리")
    void changeOptionNotFound() {
        // Given
        Long productId = 1L;
        Long optionId = 1L;
        OptionRequest optionRequest = new OptionRequest("Updated Option", 150);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> optionService.changeOption(optionRequest, productId, optionId));

        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));
        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        assertThrows(OptionNotFoundException.class, () -> optionService.changeOption(optionRequest, productId, optionId));
    }
}
