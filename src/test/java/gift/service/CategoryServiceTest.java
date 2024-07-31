package gift.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.clearInvocations;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sun.jdi.request.DuplicateRequestException;
import gift.product.dto.category.CategoryDto;
import gift.product.model.Category;
import gift.product.repository.CategoryRepository;
import gift.product.service.CategoryService;
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
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    @Test
    void 카테고리_추가() {
        //given
        CategoryDto categoryDto = new CategoryDto("테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        given(categoryRepository.findByName(categoryDto.name())).willReturn(Optional.empty());

        //when
        categoryService.insertCategory(categoryDto);

        //then
        then(categoryRepository).should().save(any());
    }

    @Test
    void 카테고리_조회() {
        //given
        CategoryDto categoryDto = new CategoryDto("테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        given(categoryRepository.findByName(categoryDto.name())).willReturn(Optional.empty());
        given(categoryRepository.save(any())).willReturn(new Category(1L, categoryDto.name(), categoryDto.color(), categoryDto.imageUrl(), categoryDto.description()));
        Long insertedCategoryId = categoryService.insertCategory(categoryDto).getId();

        given(categoryRepository.findById(insertedCategoryId)).willReturn(
            Optional.of(new Category(insertedCategoryId, categoryDto.name(), categoryDto.color(), categoryDto.imageUrl(), categoryDto.description())));

        //when
        categoryService.getCategory(insertedCategoryId);

        //then
        then(categoryRepository).should().findById(insertedCategoryId);
    }

    @Test
    void 카테고리_전체_조회() {
        //when
        categoryService.getCategoryAll();

        //then
        then(categoryRepository).should().findAll();
    }

    @Test
    void 카테고리_수정() {
        //given
        CategoryDto categoryDto = new CategoryDto("테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        given(categoryRepository.findByName(categoryDto.name())).willReturn(Optional.empty());
        given(categoryRepository.save(any())).willReturn(new Category(1L, categoryDto.name(), categoryDto.color(), categoryDto.imageUrl(), categoryDto.description()));
        Long insertedCategoryId = categoryService.insertCategory(categoryDto).getId();
        clearInvocations(categoryRepository);

        given(categoryRepository.findById(insertedCategoryId)).willReturn(
            Optional.of(new Category(insertedCategoryId, categoryDto.name(), categoryDto.color(), categoryDto.imageUrl(), categoryDto.description())));

        //when
        CategoryDto updatedCategoryDto = new CategoryDto("테스트카테고리수정", "테스트컬러", "테스트주소", "테스트설명");
        categoryService.updateCategory(insertedCategoryId, updatedCategoryDto);

        //then
        then(categoryRepository).should().save(any());
    }

    @Test
    void 카테고리_삭제() {
        //given
        CategoryDto categoryDto = new CategoryDto("테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        given(categoryRepository.findByName(categoryDto.name())).willReturn(Optional.empty());
        given(categoryRepository.save(any())).willReturn(new Category(1L, categoryDto.name(), categoryDto.color(), categoryDto.imageUrl(), categoryDto.description()));
        Long insertedCategoryId = categoryService.insertCategory(categoryDto).getId();
        clearInvocations(categoryRepository);

        given(categoryRepository.findById(insertedCategoryId)).willReturn(
            Optional.of(new Category(insertedCategoryId, categoryDto.name(), categoryDto.color(), categoryDto.imageUrl(), categoryDto.description())));

        //when
        categoryService.deleteCategory(insertedCategoryId);

        //then
        then(categoryRepository).should().deleteById(insertedCategoryId);
    }

    @Test
    void 실패_존재하지_않는_카테고리_조회() {
        //given
        given(categoryRepository.findById(-1L)).willReturn(
            Optional.empty());

        //when, then
        assertThatThrownBy(() -> categoryService.getCategory(-1L)).isInstanceOf(
            NoSuchElementException.class);
    }

    @Test
    void 실패_이미_존재하는_카테고리_추가() {
        //given
        CategoryDto categoryDto = new CategoryDto("테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        given(categoryRepository.findByName(categoryDto.name())).willReturn(Optional.empty());
        given(categoryRepository.save(any())).willReturn(new Category(1L, categoryDto.name(), categoryDto.color(), categoryDto.imageUrl(), categoryDto.description()));
        Long insertedCategoryId = categoryService.insertCategory(categoryDto).getId();

        given(categoryRepository.findByName(categoryDto.name())).willReturn(
            Optional.of(new Category(insertedCategoryId, categoryDto.name(), categoryDto.color(), categoryDto.imageUrl(), categoryDto.description())));

        //when, then
        assertThatThrownBy(
            () -> categoryService.insertCategory(categoryDto)).isInstanceOf(
            DuplicateRequestException.class);
    }

}
