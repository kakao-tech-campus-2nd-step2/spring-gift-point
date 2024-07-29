package gift.option.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.option.OptionFixture;
import gift.option.dto.OptionReqDto;
import gift.option.entity.Option;
import gift.option.exception.OptionDuplicatedNameException;
import gift.option.exception.OptionErrorCode;
import gift.option.exception.OptionNotEnoughStockException;
import gift.option.repository.OptionRepository;
import gift.product.ProductFixture;
import gift.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("옵션 서비스 테스트")
class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    @Test
    @DisplayName("옵션 추가 성공")
    void addOptions_success() {
        //given
        Product product = ProductFixture.createProduct();
        List<OptionReqDto> optionReqDtos = List.of(
                OptionFixture.createOptionReqDto("옵션1", 10),
                OptionFixture.createOptionReqDto("옵션2", 20)
        );

        //when
        optionService.addOptions(product, optionReqDtos);
        List<Option> options = product.getOptions();

        //then
        assertThatList(options).hasSize(2);
        assertThatList(options).extracting(Option::getName)
            .containsExactly("옵션1", "옵션2");
        assertThatList(options).extracting(Option::getQuantity)
            .containsExactly(10, 20);
        assertThatList(options).extracting(Option::getProduct)
            .containsOnly(product);
    }
    
    @Test
    @DisplayName("옵션 추가 실패 - 중복된 옵션 이름")
    void addOptions_fail_duplicatedNames() {
        //given
        Product product = ProductFixture.createProduct();
        List<OptionReqDto> optionReqDtos = List.of(
                OptionFixture.createOptionReqDto("옵션1", 10),
                OptionFixture.createOptionReqDto("옵션1", 20)
        );

        //then
        assertThatThrownBy(() -> optionService.addOptions(product, optionReqDtos))
            .isInstanceOf(OptionDuplicatedNameException.class)
            .hasMessage(OptionErrorCode.DUPLICATED_OPTION_NAME.getMessage());
    }

    @Test
    @DisplayName("옵션 수정 성공")
    void updateOptions_success() {
        //given
        Product product = ProductFixture.createProduct();
        List.of(
                OptionFixture.createOptionReqDto("기존 옵션1", 10),
                OptionFixture.createOptionReqDto("기존 옵션2", 20)
        ).forEach(optionReqDto -> product.addOption(optionReqDto.toEntity()));

        List<OptionReqDto> optionReqDtos = List.of(
                OptionFixture.createOptionReqDto("수정된 옵션1", 30),
                OptionFixture.createOptionReqDto("수정된 옵션2", 40),
                OptionFixture.createOptionReqDto("새로운 옵션", 50)
        );

        //when
        optionService.updateOptions(product, optionReqDtos);
        List<Option> options = product.getOptions();

        //then
        assertThatList(options).hasSize(3);
        assertThatList(options).extracting(Option::getName)
            .containsExactly("수정된 옵션1", "수정된 옵션2", "새로운 옵션");
        assertThatList(options).extracting(Option::getQuantity)
            .containsExactly(30, 40, 50);
        assertThatList(options).extracting(Option::getProduct)
            .containsOnly(product);
    }

    @Test
    @DisplayName("옵션 수정 실패 - 중복된 옵션 이름")
    void updateOptions_fail_duplicatedNames() {
        //given
        Product product = ProductFixture.createProduct();

        List<OptionReqDto> optionReqDtos = List.of(
                OptionFixture.createOptionReqDto("중복된 옵션", 10),
                OptionFixture.createOptionReqDto("중복된 옵션", 20)
        );

        //then
        assertThatThrownBy(() -> optionService.updateOptions(product, optionReqDtos))
            .isInstanceOf(OptionDuplicatedNameException.class)
            .hasMessage(OptionErrorCode.DUPLICATED_OPTION_NAME.getMessage());
    }

    @Test
    @DisplayName("옵션 재고 차감 성공")
    void subtractQuantity_success() {
        //given
        Option option = OptionFixture.createOption("테스트 옵션", 10);

        //when
        optionService.subtractQuantity(option, 5);

        //then
        assertThat(option.getQuantity()).isEqualTo(5);
    }
    
    @Test
    @DisplayName("옵션 재고 차감 실패 - 재고 부족")
    void subtractQuantity_fail_notEnoughQuantity() {
        //given
        Option option = new Option("재고 부족한 옵션", 5);

        //then
        assertThatThrownBy(() -> optionService.subtractQuantity(option, 15))
            .isInstanceOf(OptionNotEnoughStockException.class)
            .hasMessage(OptionErrorCode.NOT_ENOUGH_STOCK.getMessage());
    }
}
