package gift.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.clearInvocations;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.product.dto.option.OptionDto;
import gift.product.dto.option.OptionResponse;
import gift.product.model.Category;
import gift.product.model.Option;
import gift.product.model.Product;
import gift.product.repository.CategoryRepository;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import gift.product.service.OptionService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OptionServiceTest {

    @InjectMocks
    OptionService optionService;

    @Mock
    OptionRepository optionRepository;

    @Mock
    ProductRepository productRepository;

    @Test
    void 옵션_추가() {
        //given
        Category category = new Category("테스트카테고리");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(optionRepository.existsByNameAndProductId("테스트옵션", product.getId())).willReturn(
            false);

        //when
        optionService.insertOption(new OptionDto("테스트옵션", 1), product.getId());

        //then
        then(optionRepository).should().save(any());
    }

    @Test
    void 옵션_전체_조회() {
        //when
        optionService.getOptionAll();

        //then
        then(optionRepository).should().findAll();
    }

    @Test
    void 특정_상품의_옵션_전체_조회() {
        //given
        Category category = new Category("테스트카테고리");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        //when
        optionService.getOptionAllByProductId(product.getId());

        //then
        then(optionRepository).should().findAllByProductId(product.getId());
    }

    @Test
    void 옵션_조회() {
        //given
        Category category = new Category("테스트카테고리");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 1, product);
        given(optionRepository.findById(option.getId())).willReturn(Optional.of(option));

        //when
        optionService.getOption(option.getId());

        //then
        then(optionRepository).should().findById(option.getId());
    }

    @Test
    void 옵션_수정() {
        //given
        Category category = new Category("테스트카테고리");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 1, product);
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(optionRepository.existsByNameAndProductId("테스트옵션", product.getId())).willReturn(
            false);
        given(optionRepository.save(any())).willReturn(option);
        Long insertedOptionId = optionService.insertOption(
            new OptionDto("테스트옵션", 1), product.getId()).getId();

        given(optionRepository.findById(insertedOptionId)).willReturn(
            Optional.of(option));
        clearInvocations(optionRepository);

        //when
        optionService.updateOption(insertedOptionId, new OptionDto("테스트옵션수정", 1), product.getId());

        //then
        then(optionRepository).should().save(any());
    }

    @Test
    void 옵션_삭제() {
        //given
        Category category = new Category("테스트카테고리");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        Option option = new Option(2L, "테스트옵션2", 1, product);
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(optionRepository.findById(option.getId())).willReturn(Optional.of(option));

        OptionResponse optionResponse1 = new OptionResponse(1L, "테스트옵션1", 1);
        OptionResponse optionResponse2 = new OptionResponse(2L, "테스트옵션2", 1);
        given(optionRepository.findAllByProductId(product.getId())).willReturn(
            List.of(optionResponse1, optionResponse2));

        //when
        optionService.deleteOption(option.getId(), product.getId());

        //then
        then(optionRepository).should().deleteByIdAndProductId(option.getId(), product.getId());
    }

    @Test
    void 실패_존재하지_않는_상품에_대한_옵션_추가() {
        //given
        given(optionRepository.existsByNameAndProductId("테스트옵션", -1L)).willReturn(false);
        given(productRepository.findById(-1L)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(
            () -> optionService.insertOption(new OptionDto("테스트옵션", 1), -1L)).isInstanceOf(
            NoSuchElementException.class);
    }

    @Test
    void 실패_옵션의_대상을_존재하지_않는_상품으로_수정() {
        //given
        given(productRepository.findById(1L)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(
            () -> optionService.updateOption(1L, new OptionDto("테스트옵션수정", 1), 1L)).isInstanceOf(
            NoSuchElementException.class);
    }

    @Test
    void 실패_존재하지_않는_옵션_조회() {
        //given
        given(optionRepository.findById(any())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(
            () -> optionService.getOption(-1L)).isInstanceOf(
            NoSuchElementException.class);
    }

    @Test
    void 실패_이미_존재하는_옵션_중복_추가() {
        //given
        given(optionRepository.existsByNameAndProductId("테스트옵션중복명", 1L)).willReturn(true);

        //when, then
        assertThatThrownBy(
            () -> optionService.insertOption(new OptionDto("테스트옵션중복명", 1), 1L)).isInstanceOf(
            IllegalArgumentException.class);
    }
}
