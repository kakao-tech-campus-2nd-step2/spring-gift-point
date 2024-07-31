package gift.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;

import gift.exception.CustomException;
import gift.product.category.dto.request.CreateCategoryRequest;
import gift.product.category.dto.request.UpdateCategoryRequest;
import gift.product.category.dto.response.CategoryResponse;
import gift.product.category.entity.Category;
import gift.product.category.repository.CategoryJpaRepository;
import gift.product.category.service.CategoryService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
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
    private CategoryJpaRepository categoryRepository;

    @Test
    @DisplayName("get all categories test")
    void getAllCategoriesTest() {
        // given
        Category category1 = new Category("Category 1");
        Category category2 = new Category("Category 2");
        Category category3 = new Category("Category 3");
        List<Category> categoryList = List.of(category1, category2, category3);
        given(categoryRepository.findAll()).willReturn(categoryList);

        // when
        List<CategoryResponse> categories = categoryService.getAllCategories();

        // then
        assertThat(categories).isNotNull();
        assertThat(categories.size()).isEqualTo(3);
        then(categoryRepository).should(times(1)).findAll();
    }

    @Test
    @DisplayName("get all categories empty test")
    void getAllCategoriesEmptyTest() {
        // given
        given(categoryRepository.findAll()).willReturn(List.of());

        // when
        List<CategoryResponse> categories = categoryService.getAllCategories();

        // then
        assertThat(categories).isEmpty();
        then(categoryRepository).should(times(1)).findAll();
    }

    @Test
    @DisplayName("get category by id test")
    void getCategoryByIdTest() {
        // given
        Category category1 = new Category("Category 1", "#123456", "image", "");
        given(categoryRepository.findById(any())).willReturn(Optional.of(category1));

        // when
        categoryService.getCategory(1L);

        // then
        then(categoryRepository).should().findById(any());
    }

    @Test
    @DisplayName("get category by not exist id test")
    void getCategoryByNotExistIdTest() {
        // given
        given(categoryRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> categoryService.getCategory(999L)).isInstanceOf(
            CustomException.class);
        then(categoryRepository).should().findById(any());
    }

    @Test
    @DisplayName("create category test")
    void createCategoryTest() {
        // given
        Category category1 = new Category("Category 1", "#123456", "image", "");
        CreateCategoryRequest request = new CreateCategoryRequest("new category", "#123456",
            "image", "");
        given(categoryRepository.findAll()).willReturn(List.of(category1));
        given(categoryRepository.save(any(Category.class))).willReturn(category1);

        // when
        categoryService.createCategory(request);

        // then
        then(categoryRepository).should().save(any(Category.class));
    }

    @Test
    @DisplayName("update category test")
    void updateCategoryTest() {
        // given
        String newName = "update category";
        UpdateCategoryRequest request = new UpdateCategoryRequest(newName, "#123456", "image", "");

        Category existingCategory = new Category("Category 1", "#123456", "image", "");
        given(categoryRepository.findById(any())).willReturn(Optional.of(existingCategory));

        // when
        categoryService.updateCategory(1L, request);

        // then
        then(categoryRepository).should().findById(any());
        assertThat(existingCategory.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("update not exist category test")
    void updateNotExistCategoryTest() {
        // given
        String newName = "update category";
        UpdateCategoryRequest request = new UpdateCategoryRequest(newName, "#123456", "image", "");
        given(categoryRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> categoryService.updateCategory(1L, request))
            .isInstanceOf(CustomException.class);
        then(categoryRepository).should().findById(any());
    }

    @Test
    @DisplayName("delete category test")
    void deleteCategoryTest() {
        // given
        given(categoryRepository.existsById(any())).willReturn(true);
        willDoNothing().given(categoryRepository).deleteById(any(Long.class));

        // when
        categoryService.deleteCategory(1L);

        // then
        then(categoryRepository).should().existsById(any());
        then(categoryRepository).should().deleteById(any(Long.class));
    }

    @Test
    @DisplayName("delete not exist category test")
    void deleteNotExistCategoryTest() {
        // given
        given(categoryRepository.existsById(any())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> categoryService.deleteCategory(1L))
            .isInstanceOf(CustomException.class);
        then(categoryRepository).should().existsById(any());
        then(categoryRepository).should(times(0)).deleteById(any());
    }


}
