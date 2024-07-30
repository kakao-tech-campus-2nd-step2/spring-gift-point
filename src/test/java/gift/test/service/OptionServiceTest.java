package gift.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.service.OptionService;

public class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OptionService optionService;

    @Mock
    private BindingResult bindingResult;

    private Category category;
    private Product product;
    private Option option;
    private OptionRequest optionRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
        product = new Product("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", category);
        product.setId(1L);
        option = new Option("01 [Best] 시어버터 핸드 & 시어 스틱 립 밤", 969, product);
        option.setId(1L);
        optionRequest = new OptionRequest("01 [Best] 시어버터 핸드 & 시어 스틱 립 밤", 969);
    }

    @Test
    public void testGetOptions() {
        when(optionRepository.findByProductId(product.getId())).thenReturn(Collections.singletonList(option));
        List<OptionResponse> responses = optionService.getOptions(product.getId());
        
        verify(optionRepository).findByProductId(product.getId());
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getName()).isEqualTo(option.getName());
    }

    @Test
    public void testAddOption() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(optionRepository.save(any(Option.class))).thenReturn(option);
        optionService.addOption(product.getId(), optionRequest, bindingResult);
        
        verify(productRepository).findById(product.getId());
        verify(optionRepository, times(1)).save(any(Option.class));
    }
}
