package gift.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import gift.dto.request.OptionRequest;
import gift.dto.response.GetOptionsResponse;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OptionServiceTest {
    
    @Autowired
    private OptionService optionService;

    @MockBean
    private OptionRepository optionRepository;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void findByProductId(){
        
        //given
        Long productId = 1L;
        Category category = new Category("category", "color", "url", ""); 
        Product product = new Product("product", 0, "imageUrl", category);
        Option option = new Option(product, "option", 1);

        when(productRepository.findById(productId))
            .thenReturn(Optional.of(product));
        when(optionRepository.findByProductId(productId))
            .thenReturn(List.of(option));

        GetOptionsResponse optionResponse = optionService.findByProductId(1L);

        assertAll(
            () -> assertNotNull(optionResponse),
            () -> assertEquals(1, optionResponse.getOptions().size()),
            () -> assertEquals("option", optionResponse.getOptions().get(0).getName())
        );
    }

    @Test
    void addOption(){
        
        //given
        Long productId = 1L;
        String name = "option";
        Category category = new Category("category", "color", "url", ""); 
        Product product = new Product("product", 0, "imageUrl", category);
        OptionRequest optionRequest = new OptionRequest(name, 1);

        when(productRepository.findById(productId))
            .thenReturn(Optional.of(product));
        when(optionRepository.findByName(null))
            .thenReturn(Optional.empty());

        optionService.addOption(optionRequest, productId);

        verify(optionRepository, times(1)).save(any(Option.class));
    }

    @Test
    void substractQuantity(){

        //given
        String name = "option";
        Category category = new Category("category", "color", "url", ""); 
        Product product = new Product("product", 0, "imageUrl", category);
        Option option = new Option(product, name, 100);

        ArgumentCaptor<Option> optionCaptor = ArgumentCaptor.forClass(Option.class);

        when(optionRepository.findByName(name))
            .thenReturn(Optional.of(option));
        when(optionRepository.save(any(Option.class)))
            .thenReturn(option);

        optionService.subtractQuantity(option.getId(), 50);
        
        verify(optionRepository, times(1)).delete(option);
        verify(optionRepository, times(1)).save(optionCaptor.capture());
        Option savedOption = optionCaptor.getValue();

        assertEquals(50, savedOption.getStockQuantity());
    }
}
