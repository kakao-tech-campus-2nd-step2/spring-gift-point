package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.dto.OptionResponseDto;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.OptionException;
import gift.repository.OptionRepository;
import gift.service.OptionService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    @Test
    @DisplayName("전건조회 테스트")
    public void findAllByProductIdTest(){
        Option option = new Option("교환권", 10, new Product("치킨", 20000, "chicken.com", new Category("음식")));
        when(optionRepository.findByProductId(anyLong())).thenReturn(Arrays.asList(option));

        List<OptionResponseDto> options = optionService.findAllByProductId(1L);

        assertThat(options.size()).isEqualTo(1);
        assertThat(options.get(0).getName()).isEqualTo("교환권");

    }

    @Test
    @DisplayName("옵션 저장 테스트")
    public void saveTest(){
        Option option = new Option("교환권", 10, new Product("치킨", 20000, "chicken.com", new Category("음식")));
        when(optionRepository.save(any(Option.class))).thenReturn(option);

        Option savedOption = optionService.save(option);

        assertThat(savedOption).isNotNull();
        assertThat(savedOption.getName()).isEqualTo(option.getName());
    }

    @Test
    @DisplayName("중복 옵션 테스트")
    public void saveDuplicateTest(){
        Option option = new Option("교환권", 10, new Product("치킨", 20000, "chicken.com", new Category("음식")));
        //when(optionRepository.existsByNameAndProductId(anyString(), anyLong())).thenReturn(true);
        when(optionRepository.existsByNameAndProductId(option.getName(), option.getProduct().getId())).thenReturn(true);

        OptionException exception = assertThrows(OptionException.class, () -> {
            optionService.save(option);
        });


        assertThat("동일한 상품 내에 중복된 옵션이 있습니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("양감소 테스트")
    public void decreaseQuantityTest(){
        Option option = new Option("교환권", 10, new Product("치킨", 20000, "chicken.com", new Category("음식")));
        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));

        optionService.decreaseQuantity(1L, 5);

        assertThat(option.getQuantity()).isEqualTo(5);
        verify(optionRepository, times(1)).save(option);
    }
}
