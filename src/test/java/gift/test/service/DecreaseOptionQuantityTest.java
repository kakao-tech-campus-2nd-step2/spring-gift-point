package gift.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionRepository;
import gift.service.OptionService;

public class DecreaseOptionQuantityTest {
	
	@Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    private Product product;
    private Option option;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", null);
        product.setId(1L);

        option = new Option("01 [Best] 시어버터 핸드 & 시어 스틱 립 밤", 969, product);
        option.setId(1L);
    }
    
    @Test
    public void testDecreaseOptionQuantity() {
    	when(optionRepository.findById(option.getId())).thenReturn(Optional.of(option));

        optionService.decreaseOptionQuantity(option.getId(), 30);

        assertThat(option.getQuantity()).isEqualTo(939);
    }
}
