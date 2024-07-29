package gift;

import gift.controller.CategoryController;
import gift.domain.model.dto.CategoryAddRequestDto;
import gift.domain.model.dto.CategoryResponseDto;
import gift.domain.model.dto.CategoryUpdateRequestDto;
import gift.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryController categoryController;

    @Test
    void getAllCategoriesTest() {
        // Given
        CategoryAddRequestDto category1 = new CategoryAddRequestDto("New Category 1");
        CategoryAddRequestDto category2 = new CategoryAddRequestDto("New Category 2");
        categoryController.addCategory(category1);
        categoryController.addCategory(category2);

        // When
        List<CategoryResponseDto> categories = categoryController.getAllCategories().getBody();

        // Then
        assertFalse(categories.isEmpty());
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("New Category 1")));
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("New Category 2")));
    }

    @Test
    void addCategoryTest() {
        // Given
        CategoryAddRequestDto requestDto = new CategoryAddRequestDto("New Category");

        // When
        CategoryResponseDto response = categoryController.addCategory(requestDto).getBody();

        // Then
        assertNotNull(response);
        assertEquals("New Category", response.getName());
    }

    @Test
    void updateCategoryTest() {
        // Given
        CategoryAddRequestDto addRequestDto = new CategoryAddRequestDto("Original Category");
        CategoryResponseDto addedCategory = categoryController.addCategory(addRequestDto).getBody();

        CategoryUpdateRequestDto updateRequestDto = new CategoryUpdateRequestDto("Updated Category");

        // When
        CategoryResponseDto updatedCategory = categoryController.updateCategory(1L, updateRequestDto)
            .getBody();

        // Then
        assertNotNull(updatedCategory);
        assertEquals("Updated Category", updatedCategory.getName());
    }

    @Test
    void validDeleteCategoryTest() {
        // Given
        CategoryAddRequestDto addRequestDto = new CategoryAddRequestDto("Category to Delete");
        CategoryResponseDto addedCategory = categoryController.addCategory(addRequestDto).getBody();

        // When & Then
        assertDoesNotThrow(() -> categoryController.deleteCategory(addedCategory.getId()));

        List<CategoryResponseDto> remainingCategories = categoryController.getAllCategories()
            .getBody();
        assertFalse(remainingCategories.stream().anyMatch(c -> c.getId().equals(addedCategory.getId())));
    }

    @Test
    void invalidDeleteCategoryTest() {
        // Given
        Long nonExistentCategoryId = 9999L;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> categoryController.deleteCategory(nonExistentCategoryId));
    }
}