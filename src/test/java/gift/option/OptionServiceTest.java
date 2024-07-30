package gift.option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gift.product.Product;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {
    @InjectMocks
    private OptionService optionService;

    @Mock
    private OptionRepository optionRepository;

    /*
    - [] 이미 있는 이름의 옵션을 추가 하려는 경우
    - [] 없는 옵션을 변경 하려는 경우
    - [] 이미 있는 이름의 옵션으로 변경 하려는 경우
     */

    @Test
    void addOption() {
        //given
        OptionRequest optionRequest = optionRequest();
        Product product = product();
        Option expected = optionWithName(optionRequest.getName(), product);
        when(optionRepository.save(any(Option.class))).thenReturn(expected);

        //when
        Option actual = optionService.addOption(optionRequest, product);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("이미 있는 이름의 옵션을 추가 하려는 경우")
    @Test
    void addOptionFail() {
        //given
        OptionRequest optionRequest = optionRequest();
        Option option = option(product());
        Product product = product();
        product.getOptions().add(option);

        when(optionRepository.findAllByProductId(any(Long.class))).thenReturn(product.getOptions());

        //when, then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> optionService.addOption(optionRequest,product));

        //then
        assertThat(exception.getMessage()).isEqualTo(" 동일한 상품 내의 옵션 이름은 중복될 수 없습니다. ");
    }


    @DisplayName("없는 옵션을 변경 하려는 경우")
    @Test
    void updateOptionFail1() {
        //given
        OptionRequest optionRequest = optionRequestWithName("update");
        when(optionRepository.findById(any(Long.class))).thenThrow(
            new NoSuchElementException());

        //when //then
        assertThrows(NoSuchElementException.class, () -> optionService.updateOption(optionRequest, product().getId()));
    }

    @DisplayName("이미 있는 이름의 옵션으로 변경 하려는 경우")
    @Test
    void updateOptionFail2() {
        //given
        OptionRequest optionRequest = optionRequestWithName("exist");
        Option option = option(product());
        Option exist = new Option(null, "exist", 1, product());

        Product product = product();
        product.getOptions().add(exist);

        when(optionRepository.findById(any(Long.class))).thenReturn(Optional.of(option));
        when(optionRepository.findAllByProductId(any(Long.class))).thenReturn(product.getOptions());

        //when, then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> optionService.updateOption(optionRequest, product.getId()));

        //then
        assertThat(exception.getMessage()).isEqualTo(" 동일한 상품 내의 옵션 이름은 중복될 수 없습니다. ");
    }

    @DisplayName("등록되지 않은 옵션을 삭제 하려는 경우")
    @Test
    void deleteOptionFail() {
        //given
        Option option = option(product());
        when(optionRepository.findById(any(Long.class))).thenThrow(new NoSuchElementException());
        //when// then
        assertThrows(NoSuchElementException.class, () -> optionService.deleteOption(1L));
    }

    private Option option(Product product){
        return optionWithName("option", product);
    }

    private Option optionWithName(String name, Product product){
        return new Option(1L, name, 1, product);
    }

    private OptionRequest optionRequest(){
        return optionRequestWithName("option");
    }

    private OptionRequest optionRequestWithName(String name){
        return new OptionRequest(1L, name, 1);
    }

    private Product product(){
        return new Product(1L, "product",1,"image", 1L);
    }

}