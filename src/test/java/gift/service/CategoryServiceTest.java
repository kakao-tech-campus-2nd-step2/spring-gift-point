package gift.service;

import gift.dto.category.CategoryRequest;
import gift.dto.category.CategoryResponse;
import gift.model.category.Category;
import gift.repository.category.CategoryRepository;
import gift.service.category.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("getCategory 테스트")
    public void testGetCategory() {
        // given
        Long categoryId = 1L;
        Category category = new Category(categoryId, "testName", "testColor", "testimageUrl", "testDescription");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // when
        CategoryResponse responseCategory = categoryService.getCategory(categoryId);

        // then
        assertThat(responseCategory).isNotNull();
        assertThat(categoryId).isEqualTo(responseCategory.getId());
        assertThat(responseCategory.getName()).isEqualTo("testName");
    }

    @Test
    @DisplayName("updateCategory 테스트")
    public void testUpdateCategory() {
        // given
        Long categoryId = 1L;
        CategoryRequest categoryRequest = new CategoryRequest("updatedName", "updatedColor", "updatedImageUrl", "updatedDescription");
        Category existingCategory = new Category(categoryId, "testName", "testColor", "testimageUrl", "testDescription");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        // when
        categoryService.updateCategory(categoryId, categoryRequest);

        // then
        assertThat(existingCategory.getName()).isEqualTo("updatedName");
        assertThat(existingCategory.getColor()).isEqualTo("updatedColor");
        assertThat(existingCategory.getImageUrl()).isEqualTo("updatedImageUrl");
        assertThat(existingCategory.getDescription()).isEqualTo("updatedDescription");
    }

    @Test
    @DisplayName("deletCategory 테스트")
    public void testDeleteCategory() {
        // given
        Long categoryId = 1L;

        // when
        categoryService.deleteCategory(categoryId);

        // then
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }


}