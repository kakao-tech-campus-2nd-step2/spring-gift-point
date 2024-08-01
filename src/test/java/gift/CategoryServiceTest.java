package gift;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.category.dto.CategoryRequestDto;
import gift.category.dto.CategoryResponseDto;
import gift.category.entity.Category;
import gift.category.repository.CategoryRepository;
import gift.category.service.CategoryService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;

    /*
     * - [ ] 정상적인 경우
     */
    @Test
    public void insertCategoryTest() {
        // given
        var name = "간식";
        var imageUrl = "gansik.png";

        var categoryRequestDto = new CategoryRequestDto(name, imageUrl);

        given(categoryRepository.save(any(Category.class))).willAnswer(
            invocation -> invocation.getArgument(0));

        // when, then
        Assertions.assertThatCode(() -> categoryService.insertCategory(categoryRequestDto))
            .doesNotThrowAnyException();

        // 정상
        then(categoryRepository).should().save(any(Category.class));
    }

    /*
     * - [ ] 정상적인 경우
     * - [ ] 존재하지 않는 카테고리 조회하는 경우
     */
    @Test
    public void selectCategoryTest() {
        // given
        var categoryId = 1L;
        var name = "간식";
        var imageUrl = "gansik.png";
        var notValidCategoryId = 2L;

        var category = new Category(categoryId, name, imageUrl);

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));
        given(categoryRepository.findById(notValidCategoryId)).willReturn(Optional.empty());

        // when, then
        Assertions.assertThat(categoryService.selectCategory(categoryId)).isEqualTo(
            CategoryResponseDto.fromCategory(category));
        Assertions.assertThatThrownBy(() -> categoryService.selectCategory(notValidCategoryId))
            .isInstanceOf(NoSuchElementException.class);

        // 정상
        then(categoryRepository).should().findById(categoryId);
        // 존재하지 않는 경우
        then(categoryRepository).should().findById(notValidCategoryId);
    }

    /*
     * - [ ] 정상적인 경우
     */
    @Test
    public void selectCategoriesTest() {
        // given
        var categoryId1 = 1L;
        var name1 = "간식";
        var imageUrl1 = "gansik.png";
        var categoryId2 = 2L;
        var name2 = "식료품";
        var imageUrl2 = "sikryo.png";

        var category1 = new Category(categoryId1, name1, imageUrl1);
        var category2 = new Category(categoryId2, name2, imageUrl2);
        var categories = List.of(category1, category2);

        given(categoryRepository.findAll()).willReturn(categories);

        // when, then
        Assertions.assertThat(categoryService.selectCategories())
            .isEqualTo(categories.stream().map(CategoryResponseDto::fromCategory).toList());

        // 정상
        then(categoryRepository).should().findAll();
    }

    /*
     * - [ ] 정상적인 경우
     * - [ ] 존재하지 않는 카테고리
     */
    @Test
    public void updateCategoryTest() {
        // given
        var categoryId = 1L;
        var notValidCategoryId = 2L;
        var name = "간식";
        var imageUrl = "gansik.png";
        var newName = "견과류";
        var newImageUrl = "dog_fruit_ryu.png";

        var categoryRequestDto = new CategoryRequestDto(newName, newImageUrl);
        var category = new Category(categoryId, name, imageUrl);

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));
        given(categoryRepository.findById(notValidCategoryId)).willReturn(Optional.empty());

        // when, then
        categoryService.updateCategory(categoryId, categoryRequestDto);
        Assertions.assertThat(category.getName()).isEqualTo(newName);
        Assertions.assertThatThrownBy(
                () -> categoryService.updateCategory(notValidCategoryId, categoryRequestDto))
            .isInstanceOf(NoSuchElementException.class);

        // 정상
        then(categoryRepository).should().findById(categoryId);
        // 존재하지 않는 카테고리
        then(categoryRepository).should().findById(notValidCategoryId);
    }

    /*
     * - [ ] 정상적인 경우
     * - [ ] 존재하지 않는 카테고리
     */
    @Test
    public void deleteCategoryTest() {
        // given
        var categoryId = 1L;
        var notValidCategoryId = 2L;

        given(categoryRepository.existsById(categoryId)).willReturn(true);
        given(categoryRepository.existsById(notValidCategoryId)).willReturn(false);

        // when, then
        Assertions.assertThatCode(() -> categoryService.deleteCategory(categoryId))
            .doesNotThrowAnyException();
        Assertions.assertThatThrownBy(() -> categoryService.deleteCategory(notValidCategoryId))
            .isInstanceOf(NoSuchElementException.class);

        // 정상
        then(categoryRepository).should().existsById(categoryId);
        // 존재하지 않는 카테고리
        then(categoryRepository).should().existsById(notValidCategoryId);
    }
}
