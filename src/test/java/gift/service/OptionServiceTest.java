package gift.service;

import gift.dto.optionDto.OptionDto;
import gift.dto.optionDto.OptionResponseDto;
import gift.model.product.Category;
import gift.model.product.Option;
import gift.model.product.Product;
import gift.model.product.ProductName;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OptionServiceTest {
    /*@InjectMocks
    private OptionService optionService;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Category category;

    public OptionServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getAllOptionsById() {
        when(optionRepository.findByProductId(1L)).thenReturn(Collections.emptyList());
        List<OptionResponseDto> options = optionService.getAllOptionsById(1L);

        assertNotNull(options);
        assertTrue(options.isEmpty());
    }

    @Test
    void addNewOption() {
        Product product = new Product(category,new ProductName("product1"),1000,"qwer.com");
        OptionDto optionDto = new OptionDto("option1",1234);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        optionService.addNewOption(product.getId(), optionDto);

        verify(productRepository).findById(product.getId());
        verify(optionRepository).save(any(Option.class));
    }*/
}