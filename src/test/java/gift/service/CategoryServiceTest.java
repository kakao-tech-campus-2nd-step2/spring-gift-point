package gift.service;

import static gift.util.constants.CategoryConstants.CATEGORY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import gift.dto.category.CategoryCreateRequest;
import gift.dto.category.CategoryResponse;
import gift.dto.category.CategoryUpdateRequest;
import gift.exception.category.CategoryNotFoundException;
import gift.model.Category;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    @DisplayName("모든 카테고리 조회")
    public void testGetAllCategories() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryResponse> categories = categoryService.getAllCategories();
        assertEquals(1, categories.size());
        assertEquals("Category", categories.getFirst().name());
    }

    @Test
    @DisplayName("카테고리 ID로 조회")
    public void testGetCategoryById() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponse categoryResponse = categoryService.getCategoryById(1L);
        assertEquals("Category", categoryResponse.name());
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 ID로 조회")
    public void testGetCategoryByIdNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        assertEquals(CATEGORY_NOT_FOUND + 1, exception.getMessage());
    }

    @Test
    @DisplayName("카테고리 추가")
    public void testAddCategory() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(
            "Category",
            "#000000",
            "imageUrl",
            "description"
        );
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        CategoryResponse createdCategory = categoryService.addCategory(categoryCreateRequest);
        assertEquals("Category", createdCategory.name());
    }

    @Test
    @DisplayName("카테고리 수정")
    public void testUpdateCategory() {
        Category existingCategory = new Category(
            "Old Category",
            "#111111",
            "oldImageUrl",
            "oldDescription"
        );
        Category updatedCategory = new Category(
            "Updated Category",
            "#000000",
            "newImageUrl",
            "newDescription"
        );
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(
            "Updated Category",
            "#000000",
            "newImageUrl",
            "newDescription"
        );

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(updatedCategory);

        CategoryResponse updatedCategoryResponse = categoryService.updateCategory(
            1L,
            categoryUpdateRequest
        );
        assertEquals("Updated Category", updatedCategoryResponse.name());
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 ID로 수정")
    public void testUpdateCategoryNotFound() {
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(
            "Updated Category",
            "#000000",
            "newImageUrl",
            "newDescription"
        );

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.updateCategory(1L, categoryUpdateRequest);
        });

        assertEquals(CATEGORY_NOT_FOUND + 1, exception.getMessage());
    }
}
