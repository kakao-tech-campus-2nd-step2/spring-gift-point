/*
package gift.service;

import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OptionServiceTest {
    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OptionService optionService;

    private Option option;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product(1L, "Sample Product", 1000, "https://via.placeholder.com/150", null);
        option = new Option(1L, "Option 1", 100, product);
    }

    @Test
    void subtractOptionQuantity_Success() {
        when(optionRepository.findByProductId(1L)).thenReturn(List.of(option));

        optionService.subtractOptionQuantity(1L, "Option 1", 10);

        assertEquals(90, option.getQuantity());
        verify(optionRepository, times(1)).save(option);
    }

    @Test
    void subtractOptionQuantity_InsufficientQuantity() {
        when(optionRepository.findByProductId(1L)).thenReturn(List.of(option));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                optionService.subtractOptionQuantity(1L, "Option 1", 110));

        assertEquals("Option quantity cannot be less than 0.", exception.getMessage());
    }

    @Test
    void subtractOptionQuantity_OptionNotFound() {
        when(optionRepository.findByProductId(1L)).thenReturn(List.of(option));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                optionService.subtractOptionQuantity(1L, "Option 2", 10));

        assertEquals("Option not found for product Id:1", exception.getMessage());
    }
}

 */