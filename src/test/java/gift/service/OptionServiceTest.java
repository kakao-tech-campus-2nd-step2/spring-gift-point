package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import gift.administrator.category.Category;
import gift.administrator.option.Option;
import gift.administrator.option.OptionDTO;
import gift.administrator.option.OptionRepository;
import gift.administrator.option.OptionService;
import gift.administrator.product.Product;
import gift.error.NotFoundIdException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OptionServiceTest {

    private OptionService optionService;
    private final OptionRepository optionRepository = mock(OptionRepository.class);
    private Option option;
    private Option option1;
    private OptionDTO expected;
    private OptionDTO expected1;

    @BeforeEach
    void beforeEach() {
        optionService = new OptionService(optionRepository);
        Category category = new Category("상품권", null, null, null);
        option = new Option("L", 3, null);
        List<Option> options = new ArrayList<>(List.of(option));
        Product product = new Product("라이언", 1000, "image.jpg", category, options);
        option1 = new Option("XL", 5, null);
        option.setProduct(product);
        option1.setProduct(product);
        expected = new OptionDTO("L", 3, null);
        expected1 = new OptionDTO("XL", 5, null);
    }

    @Test
    @DisplayName("옵션 전체 조회 테스트")
    void getAllOptions() {
        //given
        given(optionRepository.findAll()).willReturn(List.of(option, option1));

        //when
        List<OptionDTO> actual = optionService.getAllOptions();

        //then
        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(OptionDTO::getName, OptionDTO::getQuantity,
            OptionDTO::getProductId).containsExactly(
            tuple(expected.getName(), expected.getQuantity(), expected.getProductId()),
            tuple(expected1.getName(), expected1.getQuantity(), expected1.getProductId()));
    }

    @Test
    @DisplayName("상품 아이디로 옵션 전체 조회 테스트")
    void getAllOptionsByProductId() {
        //given
        given(optionRepository.findAllByProductId(anyLong())).willReturn(List.of(option, option1));

        //when
        List<OptionDTO> actual = optionService.getAllOptionsByProductId(1L);

        //then
        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(OptionDTO::getName, OptionDTO::getQuantity,
            OptionDTO::getProductId).containsExactly(
            tuple(expected.getName(), expected.getQuantity(), expected.getProductId()),
            tuple(expected1.getName(), expected1.getQuantity(), expected1.getProductId()));
    }

    @Test
    @DisplayName("옵션 아이디로 옵션 전체 조회 테스트")
    void getAllOptionsByOptionId() {
        //given
        given(optionRepository.findAllById(List.of(1L, 2L))).willReturn(List.of(option, option1));

        //when
        List<OptionDTO> actual = optionService.getAllOptionsByOptionId(List.of(1L, 2L));

        //then
        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(OptionDTO::getName, OptionDTO::getQuantity,
            OptionDTO::getProductId).containsExactly(
            tuple(expected.getName(), expected.getQuantity(), expected.getProductId()),
            tuple(expected1.getName(), expected1.getQuantity(), expected1.getProductId()));
    }

    @Test
    @DisplayName("옵션 아이디와 상품 아이디로 존재 여부 확인 시 존재함")
    void findOptionById() {
        //given
        given(optionRepository.findById(anyLong())).willReturn(Optional.ofNullable(option));

        //when
        OptionDTO actual = optionService.findOptionById(1L);

        //then
        assertThat(actual).extracting(OptionDTO::getName, OptionDTO::getQuantity,
                OptionDTO::getProductId)
            .containsExactly(expected.getName(), expected.getQuantity(), expected.getProductId());
    }

    @Test
    @DisplayName("옵션 아이디와 상품 아이디로 존재 여부 확인 시 존재하지 않음")
    void findOptionByIdNotFound() {
        //given
        given(optionRepository.findById(anyLong())).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> optionService.findOptionById(1L)).isInstanceOf(
            NotFoundIdException.class).hasMessageContaining("아이디를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("옵션 아이디와 상품 아이디로 존재 여부 확인 시 존재함")
    void existsByOptionIdAndProductId() {
        //given
        given(optionRepository.existsByIdAndProductId(anyLong(), anyLong())).willReturn(true);

        //when
        boolean actual = optionService.existsByOptionIdAndProductId(1L, 1L);

        //then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("옵션 아이디와 상품 아이디로 존재 여부 확인 시 존재하지 않음")
    void notExistsByOptionIdAndProductId() {
        //given
        given(optionRepository.existsByIdAndProductId(anyLong(), anyLong())).willReturn(false);

        //when
        boolean actual = optionService.existsByOptionIdAndProductId(1L, 1L);

        //then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("옵션 이름과 상품 아이디가 같지만 옵션 아이디는 다를 때 throw하지 않음")
    void existsByNameSameProductIdNotOptionId() {
        //given
        given(optionRepository.existsByNameAndProductIdAndIdNot(anyString(), anyLong(),  anyLong()))
            .willReturn(false);

        //when

        //then
        assertDoesNotThrow(() -> optionService.existsByNameSameProductIdNotOptionId("라면", 1L, 1L));
    }

    @Test
    @DisplayName("옵션 이름과 상품 아이디가 다르거나 옵션 아이디가 같을 때 throw")
    void existsByNameSameProductIdNotOptionIdThrowsException() {
        //given
        given(optionRepository.existsByNameAndProductIdAndIdNot(anyString(), anyLong(), anyLong()))
            .willReturn(true);

        //when

        //then
        assertThatIllegalArgumentException().isThrownBy(
                () -> optionService.existsByNameSameProductIdNotOptionId("라면", 1L, 1L))
            .withMessage("같은 상품 내에서 동일한 이름의 옵션은 불가합니다.");
    }

    @Test
    @DisplayName("옵션 아이디로 찾은 상품 아이디로 모든 옵션 찾기")
    void countAllOptionsByProductIdFromOptionId() {
        //given
        given(optionRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(option));
        given(optionRepository.countAllByProductId(any()))
            .willReturn(2);

        //when
        int actual = optionService.countAllOptionsByProductIdFromOptionId(1L);

        //then
        assertThat(actual).isEqualTo(2);
    }

    @Test
    @DisplayName("옵션 업데이트 하기 성공")
    void updateOption() {
        //given
        given(optionRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(option));
        given(optionRepository.save(any(Option.class)))
            .willReturn(option);

        //when
        OptionDTO actual = optionService.updateOption(1L, OptionDTO.fromOption(option));

        //then
        assertThat(actual)
            .extracting(OptionDTO::getName, OptionDTO::getQuantity, OptionDTO::getProductId)
            .containsExactly(expected.getName(), expected.getQuantity(), expected.getProductId());
    }

    @Test
    @DisplayName("옵션 업데이트 시도 중 없는 옵션 아이디로 시도하여 exception 발생")
    void updateOptionThrowsExceptionWhenOptionIdNotFound() {
        //given
        given(optionRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> optionService.countAllOptionsByProductIdFromOptionId(1L))
            .isInstanceOf(NotFoundIdException.class)
            .hasMessageContaining("옵션 아이디를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("옵션 수량 차감 성공")
    void subtractOptionQuantity() {
        //given
        given(optionRepository.findById(anyLong())).willReturn(Optional.ofNullable(option));
        Option expected = new Option("L", 1, null);

        //when
        Option actual = optionService.subtractOptionQuantity(1L, 2);

        //then
        assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity());
    }

    @Test
    @DisplayName("옵션 수량 차감 시도 중 옵션 아이디 찾기 실패")
    void subtractOptionQuantityIdNotFound() {
        //given
        given(optionRepository.findById(anyLong())).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> optionService.subtractOptionQuantity(1L, 2)).isInstanceOf(
            NotFoundIdException.class).hasMessageContaining("옵션 아이디를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("옵션 수량 차감 시도 중 옵션 수량보다 차감하려는 수량이 더 많을 때")
    void subtractOptionQuantityErrorIfNotPossible() {
        //given
        given(optionRepository.findById(1L)).willReturn(Optional.ofNullable(option));

        //when

        //then
        assertThatIllegalArgumentException().isThrownBy(
                () -> optionService.subtractOptionQuantityErrorIfNotPossible(1L, 10))
            .withMessage("옵션의 재고가 부족합니다.");
    }

    @Test
    @DisplayName("옵션 아이디로 옵션 삭제")
    void deleteOptionByOptionId() {
        //given
        given(optionRepository.existsById(anyLong())).willReturn(true);
        given(optionRepository.findById(anyLong())).willReturn(Optional.ofNullable(option));

        //when
        optionService.deleteOptionByOptionId(1L);

        //then
        then(optionRepository).should().deleteById(any());
    }

    @Test
    @DisplayName("옵션 아이디로 옵션 삭제시 존재하지 않는 아이디로 삭제 시도")
    void deleteOptionByOptionIdIdNotFound() {
        //given
        given(optionRepository.existsById(anyLong())).willReturn(false);

        //when

        //then
        assertThatThrownBy(() -> optionService.deleteOptionByOptionId(1L)).isInstanceOf(
            NotFoundIdException.class).hasMessageContaining("옵션 아이디를 찾을 수 없습니다.");
    }
}
