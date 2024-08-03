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
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        //when
        categoryService.getCategory(category.getId());

        //then
        then(categoryRepository).should().findById(category.getId());
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
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        //when
        CategoryDto updatedCategoryDto = new CategoryDto("테스트카테고리수정", "테스트컬러", "테스트주소", "테스트설명");
        categoryService.updateCategory(category.getId(), updatedCategoryDto);

        //then
        then(categoryRepository).should().save(any());
    }

    @Test
    void 카테고리_삭제() {
        //given
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        //when
        categoryService.deleteCategory(category.getId());

        //then
        then(categoryRepository).should().deleteById(category.getId());
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
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        given(categoryRepository.findByName(category.getName())).willReturn(Optional.of(category));

        CategoryDto categoryDto = new CategoryDto(category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());

        //when, then
        assertThatThrownBy(
            () -> categoryService.insertCategory(categoryDto)).isInstanceOf(
            DuplicateRequestException.class);
    }

}
